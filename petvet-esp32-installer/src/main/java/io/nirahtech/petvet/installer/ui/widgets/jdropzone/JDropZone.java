package io.nirahtech.petvet.installer.ui.widgets.jdropzone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class JDropZone extends JPanel {


    private Runnable onClick = null;
    private File file = null;
    private Consumer<File> onDroppedFileEventHandler = null;
    private final Color defaultBackgroundColor = getBackground();
    private final Color highlightColor = new Color(100, 100, 100);

    private JLabel label;

    public JDropZone() {
        super(new BorderLayout());

        label = new JLabel("Drop an Arduino Sketch file (*.ino) here", SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBorder(new EmptyBorder(100, 100, 100, 100));
        add(label, BorderLayout.CENTER);

        this.setDropTarget(new DropTarget(this, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                    setBackground(highlightColor); 
                } else {
                    dtde.rejectDrag();
                }
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
                // No specific action needed when drop action changes
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                setBackground(defaultBackgroundColor); 
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        Transferable transferable = dtde.getTransferable();

                        @SuppressWarnings("unchecked")
                        List<File> droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        if (!droppedFiles.isEmpty()) {
                            file = droppedFiles.get(0);
                            if (file.getName().endsWith(".ino")) {
                                if (Objects.nonNull(onDroppedFileEventHandler)) {
                                    onDroppedFileEventHandler.accept(file);
                                }
                            }
                        }

                        dtde.dropComplete(true);
                    } else {
                        dtde.rejectDrop();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    dtde.dropComplete(false);
                }
            }
        }));

        this.addMouseListener(new MouseListener() {

            private final int delta = 10;
            private final Color defaultColor = getBackground();
            private final Color hoverColor = new Color(this.defaultColor.getRed()+delta, this.defaultColor.getGreen()+delta, this.defaultColor.getBlue()+delta);


            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(this.hoverColor);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(this.defaultColor);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.nonNull(onClick)) {
                    onClick.run();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }
        });
    }

    public void setOnDroppedFileEventHandler(Consumer<File> onDroppedFileEventHandler) {
        this.onDroppedFileEventHandler = onDroppedFileEventHandler;
    }

    public Optional<File> getFile() {
        return Optional.ofNullable(this.file);
    }

    public void setOnClick(final Runnable onClick) {
        this.onClick = onClick;
    }
}
