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
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.ElectronicCard;

public class ClusterLandPanel extends JPanel {

    private final Set<ElectronicCardSprite> electronicCardSprites = new HashSet<>();

    private ElectronicCard selectedChipBoard = null;
    private boolean isMousePressedOnChipBoard = false;
    private final Cluster cluster;

    ClusterLandPanel(final Cluster cluster) {
        super(new BorderLayout());
        this.setBackground(Color.BLACK);
        this.cluster = cluster;
        this.cluster.nodes().forEach(node -> {
            final ElectronicCardSprite sprite = new ElectronicCardSprite((ElectronicCard)node, this::repaint);
            sprite.setCenter(new Point(200, 200));
            this.electronicCardSprites.add(sprite);
            this.selectedChipBoard = sprite.getElectronicalCard();
        });
        final Runnable repaintReference = this::repaint;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                final Point clickedPoint = event.getPoint();
                final Optional<ElectronicCard> potentialSelectedChipBoard = retriveSelectedChipBoard(clickedPoint);
                if (potentialSelectedChipBoard.isPresent()) {
                    selectedChipBoard = potentialSelectedChipBoard.get();
                } else {
                    try {
                        ElectronicCard newNode = cluster.generateNode();
                        final ElectronicCardSprite newSprite = new ElectronicCardSprite(newNode, repaintReference);
                        newSprite.setCenter(clickedPoint);
                        electronicCardSprites.add(newSprite);
                        selectedChipBoard = newSprite.getElectronicalCard();
                    } catch (UnknownHostException e) {
                        
                    }
                }

                repaint();
            }

            @Override
            public void mousePressed(MouseEvent event) {
                retriveSelectedChipBoard(event.getPoint()).ifPresent((electronicalCard) -> {
                    selectedChipBoard = electronicalCard;
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
                if (Objects.nonNull(selectedChipBoard) && isMousePressedOnChipBoard) {
                    electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().equals(selectedChipBoard)).findFirst().ifPresent((sprite) -> {
                        sprite.setCenter(event.getPoint());
                        repaint();
                    });
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (Objects.nonNull(selectedChipBoard)) {
                        cluster.deleteNode(selectedChipBoard);
                        ElectronicCardSprite spriteToDelete = electronicCardSprites.stream().filter(sprite -> sprite.getElectronicalCard().equals(selectedChipBoard)).findFirst().get();
                        electronicCardSprites.remove(spriteToDelete);
                        selectedChipBoard = null;
                        repaint();
                    }
                }
            }
        });

        // this.cluster.addEventListenerOn(null, null);


        this.setFocusable(true);
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


        graphics2D.dispose();
    }


}
