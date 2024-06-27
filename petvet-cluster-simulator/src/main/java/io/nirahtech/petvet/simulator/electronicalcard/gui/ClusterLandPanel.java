package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import javax.swing.JPanel;

import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.ElectronicCard;

public class ClusterLandPanel extends JPanel {

    private final Set<ElectronicCardSprite> electronicCardSprites = new HashSet<>();

    private ElectronicCard selectedElectronicCard;
    private boolean isMousePressedOnChipBoard = false;
    private Consumer<ElectronicCard> eventListerOnElectronicCarSelected = null;
    private final Map<ElectronicCardSprite, Map<ElectronicCardSprite, Float>> detectedSignalsStrenghtsBySprite = new HashMap<>();
    private final Cluster cluster;

    ClusterLandPanel(final Cluster cluster) {
        super(new BorderLayout());
        this.setBackground(Color.BLACK);
        this.cluster = cluster;
        this.cluster.nodes().forEach(node -> {
            final ElectronicCardSprite sprite = new ElectronicCardSprite((ElectronicCard)node, this::repaint);
            this.detectedSignalsStrenghtsBySprite.put(sprite, new HashMap<>());
            sprite.setCenter(new Point(200, 200));
            this.electronicCardSprites.add(sprite);
            this.selectedElectronicCard = sprite.getElectronicalCard();
            if (Objects.nonNull(this.eventListerOnElectronicCarSelected)) {
                this.eventListerOnElectronicCarSelected.accept(this.selectedElectronicCard);
            }
        });
        this.updateSignalStrengthsBetweenSprites();
        final Runnable repaintReference = this::repaint;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                final Point clickedPoint = event.getPoint();
                final Optional<ElectronicCard> potentialSelectedChipBoard = retriveSelectedChipBoard(clickedPoint);
                if (potentialSelectedChipBoard.isPresent()) {
                    selectedElectronicCard = potentialSelectedChipBoard.get();
                    if (Objects.nonNull(eventListerOnElectronicCarSelected)) {
                        eventListerOnElectronicCarSelected.accept(selectedElectronicCard);
                    }
                } else {
                    try {
                        ElectronicCard newNode = cluster.generateNode();
                        final ElectronicCardSprite newSprite = new ElectronicCardSprite(newNode, repaintReference);
                        detectedSignalsStrenghtsBySprite.put(newSprite, new HashMap<>());
                        newSprite.setCenter(clickedPoint);
                        electronicCardSprites.add(newSprite);
                        selectedElectronicCard = newSprite.getElectronicalCard();
                        if (Objects.nonNull(eventListerOnElectronicCarSelected)) {
                            eventListerOnElectronicCarSelected.accept(selectedElectronicCard);
                        }
                        updateSignalStrengthsBetweenSprites();
                    } catch (UnknownHostException e) {
                        
                    }
                }

                repaint();
            }

            @Override
            public void mousePressed(MouseEvent event) {
                retriveSelectedChipBoard(event.getPoint()).ifPresent((electronicalCard) -> {
                    selectedElectronicCard = electronicalCard;
                    if (Objects.nonNull(eventListerOnElectronicCarSelected)) {
                        eventListerOnElectronicCarSelected.accept(selectedElectronicCard);
                    }
                    isMousePressedOnChipBoard = true;
                    repaint();
                });
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                if (isMousePressedOnChipBoard) {
                    isMousePressedOnChipBoard = false;
                    repaint();
                }
            }

        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                if (Objects.nonNull(selectedElectronicCard) && isMousePressedOnChipBoard) {
                    electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().equals(selectedElectronicCard)).findFirst().ifPresent((sprite) -> {
                        sprite.setCenter(event.getPoint());

                        repaint();
                    });

                    updateSignalStrengthsBetweenSprites();
                    repaint();
                }
            }

        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (Objects.nonNull(selectedElectronicCard)) {
                        cluster.deleteNode(selectedElectronicCard);
                        ElectronicCardSprite spriteToDelete = electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().equals(selectedElectronicCard)).findFirst().get();
                        electronicCardSprites.remove(spriteToDelete);
                        selectedElectronicCard = null;
                        if (Objects.nonNull(eventListerOnElectronicCarSelected)) {
                            eventListerOnElectronicCarSelected.accept(selectedElectronicCard);
                        }
                        repaint();
                    }
                }
            }
        });

        this.cluster.addEventListenerOn(MessageType.HEARTBEAT, () -> {

        });
        this.cluster.addEventListenerOn(MessageType.SCAN_NOW, () -> {

        });
        this.cluster.addEventListenerOn(MessageType.SCAN_REPORT, () -> {

        });


        this.setFocusable(true);
    }


    private void updateSignalStrengthsBetweenSprites() {
        for (ElectronicCardSprite currentSprite : electronicCardSprites) {
            for (ElectronicCardSprite otherSprite : electronicCardSprites) {
                if (!currentSprite.equals(otherSprite)) {
                    final float signalPercent = currentSprite.getSignalPercent(otherSprite);
                    if (signalPercent <= 0) {
                        if (detectedSignalsStrenghtsBySprite.get(currentSprite).containsKey(otherSprite)) {
                            detectedSignalsStrenghtsBySprite.get(currentSprite).remove(otherSprite);
                        }
                    } else {
                        detectedSignalsStrenghtsBySprite.get(currentSprite).put(otherSprite, signalPercent);
                    }
                }
            }
            final Map<ElectronicCard, Map<ElectronicCard, Float>> signals = new HashMap<>();
            detectedSignalsStrenghtsBySprite.entrySet().forEach(set -> {
                Map<ElectronicCard, Float> map = new HashMap<>();
                set.getValue().entrySet().forEach(value -> {
                    map.put(value.getKey().getElectronicalCard(), value.getValue());
                });
                signals.put(set.getKey().getElectronicalCard(), map);
            });
            this.cluster.setNeigborsNodeSignals(signals);
        }
    }

    private Optional<ElectronicCard> retriveSelectedChipBoard(Point clickedPoint) {
        Optional<ElectronicCard> potentialSelectedChipBoard = Optional.empty();
        for (ElectronicCardSprite chipBoardSprite : this.electronicCardSprites) {
            final Point point = chipBoardSprite.getCenter();
            final ElectronicCard chipBoard = chipBoardSprite.getElectronicalCard();
            final int zoneX = (int) ((point.x - (chipBoard.getWidth() / 2)));
            final int zoneY = (int) ((point.y - (chipBoard.getHeight() / 2)));
            final Rectangle collisionArea = new Rectangle(zoneX, zoneY, (int) (chipBoard.getWidth()),
                    (int) (chipBoard.getHeight()));
            if (collisionArea.contains(clickedPoint)) {
                potentialSelectedChipBoard = Optional.of(chipBoard);
                break;
            }
        }

        return potentialSelectedChipBoard;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        setBackground(Color.BLACK);

        final Graphics2D graphics2D = (Graphics2D) graphics.create();

        for (ElectronicCardSprite chipBoardSprite : this.electronicCardSprites) {
            chipBoardSprite.draw(graphics2D);
        }

        this.detectedSignalsStrenghtsBySprite.entrySet().forEach(group -> {
            group.getValue().entrySet().forEach(neighbor -> {
                final ElectronicCardSprite current = group.getKey();
                final ElectronicCardSprite other = neighbor.getKey();
                graphics2D.setColor(SignalColor.getSignalColor(neighbor.getValue()));
                graphics2D.drawLine(current.getCenter().x, current.getCenter().y,other.getCenter().x, other.getCenter().y );
            });
        });


        graphics2D.dispose();
    }

    public void setOnElectronicCardSelected(Consumer<ElectronicCard> eventListerOnElectronicCarSelected) {
        this.eventListerOnElectronicCarSelected = eventListerOnElectronicCarSelected;
    }


}
