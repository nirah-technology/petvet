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

    private ElectronicCardSprite selectedElectronicCardSprite;
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
            if (Objects.nonNull(this.selectedElectronicCardSprite)) {
                this.selectedElectronicCardSprite.setSelected(false);
            }
            this.selectedElectronicCardSprite = sprite;
            this.selectedElectronicCardSprite.setSelected(true);
            if (Objects.nonNull(this.eventListerOnElectronicCarSelected)) {
                this.eventListerOnElectronicCarSelected.accept(sprite.getElectronicalCard());
            }
        });
        this.updateSignalStrengthsBetweenSprites();
        final Runnable repaintReference = this::repaint;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                final Point clickedPoint = event.getPoint();
                final Optional<ElectronicCardSprite> potentialSelectedChipBoard = retriveSelectedChipBoard(clickedPoint);
                if (potentialSelectedChipBoard.isPresent()) {
                    if (Objects.nonNull(selectedElectronicCardSprite)) {
                        selectedElectronicCardSprite.setSelected(false);
                    }
                    selectedElectronicCardSprite = potentialSelectedChipBoard.get();
                    selectedElectronicCardSprite.setSelected(true);
                    if (Objects.nonNull(eventListerOnElectronicCarSelected)) {
                        eventListerOnElectronicCarSelected.accept(selectedElectronicCardSprite.getElectronicalCard());
                    }
                } else {
                    try {
                        ElectronicCard newNode = cluster.generateNode();
                        final ElectronicCardSprite newSprite = new ElectronicCardSprite(newNode, repaintReference);
                        detectedSignalsStrenghtsBySprite.put(newSprite, new HashMap<>());
                        newSprite.setCenter(clickedPoint);
                        electronicCardSprites.add(newSprite);
                        if (Objects.nonNull(selectedElectronicCardSprite)) {
                            selectedElectronicCardSprite.setSelected(false);
                        }
                            selectedElectronicCardSprite = newSprite;
                            selectedElectronicCardSprite.setSelected(true);
                            if (Objects.nonNull(eventListerOnElectronicCarSelected)) {
                                eventListerOnElectronicCarSelected.accept(selectedElectronicCardSprite.getElectronicalCard());
                            }
                        updateSignalStrengthsBetweenSprites();
                    } catch (UnknownHostException e) {
                        
                    }
                }

                repaint();
            }

            @Override
            public void mousePressed(MouseEvent event) {
                retriveSelectedChipBoard(event.getPoint()).ifPresent((selectedSprite) -> {
                    if (Objects.nonNull(selectedElectronicCardSprite)) {
                        selectedElectronicCardSprite.setSelected(false);
                    }
                    selectedElectronicCardSprite = selectedSprite;
                    selectedElectronicCardSprite.setSelected(true);
                    if (Objects.nonNull(eventListerOnElectronicCarSelected)) {
                        eventListerOnElectronicCarSelected.accept(selectedElectronicCardSprite.getElectronicalCard());
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
                if (Objects.nonNull(selectedElectronicCardSprite) && isMousePressedOnChipBoard) {
                    electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().equals(selectedElectronicCardSprite.getElectronicalCard())).findFirst().ifPresent((sprite) -> {
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
                    if (Objects.nonNull(selectedElectronicCardSprite)) {
                        cluster.deleteNode(selectedElectronicCardSprite.getElectronicalCard());
                        ElectronicCardSprite spriteToDelete = electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().equals(selectedElectronicCardSprite.getElectronicalCard())).findFirst().get();
                        electronicCardSprites.remove(spriteToDelete);
                        selectedElectronicCardSprite = null;
                        if (Objects.nonNull(eventListerOnElectronicCarSelected)) {
                            eventListerOnElectronicCarSelected.accept(null);
                        }
                        repaint();
                    }
                }
            }
        });

        this.cluster.addEventListenerOn(MessageType.HEARTBEAT, (emitterId) -> {
            System.out.println("Heartbeat: " + emitterId);
        });
        this.cluster.addEventListenerOn(MessageType.SCAN_NOW, (emitterId) -> {

        });
        this.cluster.addEventListenerOn(MessageType.SCAN_REPORT, (emitterId) -> {

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

    private Optional<ElectronicCardSprite> retriveSelectedChipBoard(Point clickedPoint) {
        Optional<ElectronicCardSprite> potentialSelectedChipBoard = Optional.empty();
        for (ElectronicCardSprite sprite : this.electronicCardSprites) {
            final Point point = sprite.getCenter();
            final ElectronicCard chipBoard = sprite.getElectronicalCard();
            final int zoneX = (int) ((point.x - (chipBoard.getWidth() / 2)));
            final int zoneY = (int) ((point.y - (chipBoard.getHeight() / 2)));
            final Rectangle collisionArea = new Rectangle(zoneX, zoneY, (int) (chipBoard.getWidth()),
                    (int) (chipBoard.getHeight()));
            if (collisionArea.contains(clickedPoint)) {
                potentialSelectedChipBoard = Optional.of(sprite);
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
