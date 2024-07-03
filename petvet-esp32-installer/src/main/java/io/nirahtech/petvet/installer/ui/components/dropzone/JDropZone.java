package io.nirahtech.petvet.installer.ui.components.dropzone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
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
                // No specific action needed while dragging over
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
                        List<File> droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        if (!droppedFiles.isEmpty()) {
                            file = droppedFiles.get(0); // Get the first file only
                            System.out.println(file.getName());
                            if (file.getName().endsWith(".ino")) {
                                label.setText(file.getName()); 
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
    }

    public void setOnDroppedFileEventHandler(Consumer<File> onDroppedFileEventHandler) {
        this.onDroppedFileEventHandler = onDroppedFileEventHandler;
    }

    public Optional<File> getFile() {
        return Optional.ofNullable(this.file);
    }
}
