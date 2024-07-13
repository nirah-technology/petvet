package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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
import io.nirahtech.petvet.simulator.electronicalcard.MicroController;
import io.nirahtech.petvet.simulator.electronicalcard.gui.widgets.JCrossDirectionPanel;
import io.nirahtech.petvet.simulator.electronicalcard.gui.widgets.JZoomPanel;

public class ClusterLandPanel extends JPanel {

    private final Set<ElectronicCardSprite> electronicCardSprites = new HashSet<>();

    private ElectronicCardSprite selectedElectronicCardSprite;
    private boolean isMousePressedOnChipBoard = false;
    private Consumer<ElectronicCard> eventListerOnElectronicCarSelected = null;
    private Runnable eventListerOnElectronicCarMoved = null;
    private Consumer<ElectronicCard> eventListerOnElectronicCarCreated = null;
    
    private final Map<ElectronicCardSprite, Map<ElectronicCardSprite, Float>> detectedSignalsStrenghtsBySprite = new HashMap<>();
    private final Cluster cluster;

    ClusterLandPanel(final Cluster cluster) {
        super(new BorderLayout());
        this.setBackground(Color.BLACK);
        this.cluster = cluster;
        setADefaultRandomPositionForNodesInCluster();
        this.updateSignalStrengthsBetweenSprites();
        final Runnable repaintReference = this::repaint;





        final JCrossDirectionPanel crossDirectionPanel = new JCrossDirectionPanel();
        final JZoomPanel zoomPanel = new JZoomPanel();
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.add(zoomPanel, BorderLayout.NORTH);
        container.add(crossDirectionPanel, BorderLayout.SOUTH);
        bottomRightPanel.add(container);
        bottomRightPanel.setOpaque(false);
        // bottomRightPanel.setBackground(new Color(0,0,0,0));
        this.add(bottomRightPanel, BorderLayout.SOUTH);








        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                final Point clickedPoint = event.getPoint();
                final Optional<ElectronicCardSprite> potentialSelectedChipBoard = retriveSelectedChipBoard(clickedPoint);
                if (potentialSelectedChipBoard.isPresent()) {
                    changeSelectedElectronicCard(potentialSelectedChipBoard);
                } else {
                    createANewElectronicCardAndSelectedIt(cluster, repaintReference, clickedPoint);
                }
                repaint();
            }

            private void createANewElectronicCardAndSelectedIt(final Cluster cluster, final Runnable repaintReference, final Point clickedPoint) {
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
                    if (Objects.nonNull(eventListerOnElectronicCarMoved)) {
                        eventListerOnElectronicCarMoved.run();
                    }
                    if (Objects.nonNull(eventListerOnElectronicCarCreated)) {
                        eventListerOnElectronicCarCreated.accept(newNode);
                    }
                } catch (UnknownHostException e) {
                    
                }
            }

            private void changeSelectedElectronicCard(final Optional<ElectronicCardSprite> potentialSelectedChipBoard) {
                if (Objects.nonNull(selectedElectronicCardSprite)) {
                    selectedElectronicCardSprite.setSelected(false);
                }
                selectedElectronicCardSprite = potentialSelectedChipBoard.get();
                selectedElectronicCardSprite.setSelected(true);
                if (Objects.nonNull(eventListerOnElectronicCarSelected)) {
                    eventListerOnElectronicCarSelected.accept(selectedElectronicCardSprite.getElectronicalCard());
                }
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
                moveSelectedElectronicCard(event);
            }

            private void moveSelectedElectronicCard(MouseEvent event) {
                if (Objects.nonNull(selectedElectronicCardSprite) && isMousePressedOnChipBoard) {
                    electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().equals(selectedElectronicCardSprite.getElectronicalCard())).findFirst().ifPresent((sprite) -> {
                        sprite.setCenter(event.getPoint());
                        repaint();
                    });

                    updateSignalStrengthsBetweenSprites();
                    if (Objects.nonNull(eventListerOnElectronicCarMoved)) {
                        eventListerOnElectronicCarMoved.run();
                    }
                    repaint();

                }
            }

        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteSelectedElectronicCard();
                }
            }
            private void deleteSelectedElectronicCard() {
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
        });

        this.cluster.addEventListenerOn(MessageType.HEARTBEAT, (emitterId) -> {
            electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().getProcess().getId().equals(emitterId)).findFirst().ifPresent((sprite) -> {
                sprite.triggerHeartBeat();
            });
        });
        this.cluster.addEventListenerOn(MessageType.SCAN_NOW, (emitterId) -> {
            electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().getProcess().getId().equals(emitterId)).findFirst().ifPresent((sprite) -> {
                sprite.triggerScanOrder();
            });
        });
        this.cluster.addEventListenerOn(MessageType.SCAN_REPORT, (emitterId) -> {
            electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().getProcess().getId().equals(emitterId)).findFirst().ifPresent((sprite) -> {
                sprite.triggerEchoRadar();
            });
        });


        this.setFocusable(true);
    }


    private void setADefaultRandomPositionForNodesInCluster() {
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

    /**
     * @param eventListerOnElectronicCarCreated the eventListerOnElectronicCarCreated to set
     */
    public void setEventListerOnElectronicCarCreated(Consumer<ElectronicCard> eventListerOnElectronicCarCreated) {
        this.eventListerOnElectronicCarCreated = eventListerOnElectronicCarCreated;
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
        for (ElectronicCardSprite sprite : this.electronicCardSprites) {
            sprite.draw(graphics2D);
        }

        graphics2D.dispose();
    }

    public void setOnElectronicCardSelected(Consumer<ElectronicCard> eventListerOnElectronicCarSelected) {
        this.eventListerOnElectronicCarSelected = eventListerOnElectronicCarSelected;
    }


    public void setOnElectronicCardMoved(Runnable eventListerOnElectronicCarMoved) {
        this.eventListerOnElectronicCarMoved = eventListerOnElectronicCarMoved;
    }


    public void setSelectedMicroController(MicroController microController) {
        if (Objects.nonNull(microController)) {
            this.electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().equals(microController)).findFirst().ifPresent(sprite -> {
                if (Objects.nonNull(selectedElectronicCardSprite)) {
                    selectedElectronicCardSprite.setSelected(false);
                }
                selectedElectronicCardSprite = sprite;
                selectedElectronicCardSprite.setSelected(true);
                if (Objects.nonNull(eventListerOnElectronicCarSelected)) {
                    eventListerOnElectronicCarSelected.accept(selectedElectronicCardSprite.getElectronicalCard());
                }
                this.revalidate();
                this.repaint();
            });
        }
    }

}
