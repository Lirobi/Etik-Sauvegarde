package fr.lbischung.view;

import fr.lbischung.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;


public class Display extends JFrame {

    private final Controller _controller;

    private JFrame _frame = new JFrame();

    private JPanel _mainPanel = new JPanel();

    private JPanel _pathsPanel = new JPanel();

    private JLabel _lblSourcePath = new JLabel();
    private JLabel _lblTargetPath = new JLabel();

    private JButton _btnSourcePath = new JButton();
    private JButton _btnTargetPath = new JButton();

    private JButton _btnSave = new JButton();

    private int WIDTH = 800;
    private int HEIGHT = 300;

    public Display(Controller controller) throws Exception {
        _controller = controller;
        this.initWindow();
        this.initSystemTray();
        this.initHomePage();
    }

    private void initWindow() {
        _frame.setTitle("ETiK Sauvegarde");
        _frame.setSize(WIDTH, HEIGHT);
        _frame.setLocationRelativeTo(null);
        _frame.setResizable(false);
        _frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        _frame.setVisible(true);
    }

    private void initSystemTray() throws AWTException {

        BufferedImage img =  new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.BLUE);
        g.fillOval(0, 0, 64, 64);
        g.dispose();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                _frame.setVisible(false);
            }
        });

        TrayIcon icon = new TrayIcon(img);
        icon.addActionListener( evt->{
            _frame.setVisible(true);
        });
        icon.setToolTip("Etik Sauvegarde");


        PopupMenu popup = new PopupMenu();

        MenuItem openItem = new MenuItem("Open");
        openItem.addActionListener(evt->{
           _frame.setVisible(true);
        });
        popup.add(openItem);

        MenuItem exitItem = new MenuItem("Close");
        exitItem.addActionListener(evt->{
            System.exit(0);
        });
        popup.add(exitItem);

        icon.setPopupMenu(popup);
        SystemTray.getSystemTray().add(icon);
    }

    private void initHomePage() {
        _mainPanel.setLayout(new BorderLayout());

        JLabel h1 = new JLabel("ETiK Sauvegarde");
        h1.setHorizontalAlignment(SwingConstants.CENTER);

        _mainPanel.add(h1, BorderLayout.NORTH);

        _pathsPanel.setLayout(new GridBagLayout());



        _lblSourcePath.setText(_controller.getSourcePath());
        _lblSourcePath.setHorizontalAlignment(SwingConstants.CENTER);

        _lblTargetPath.setText(_controller.getTargetPath());
        _lblTargetPath.setHorizontalAlignment(SwingConstants.CENTER);

        _btnSourcePath.setText("Change source");
        _btnSourcePath.setHorizontalAlignment(SwingConstants.CENTER);

        _btnTargetPath.setText("Change target");
        _btnTargetPath.setHorizontalAlignment(SwingConstants.CENTER);


        _btnSourcePath.addActionListener(evt->{
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(_frame);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                _controller.setSourcePath(chooser.getSelectedFile().getAbsolutePath());
            }
            _lblSourcePath.setText(_controller.getSourcePath());
        });

        _btnTargetPath.addActionListener(evt->{
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(_frame);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                _controller.setTargetPath(chooser.getSelectedFile().getAbsolutePath());
            }
            _lblTargetPath.setText(_controller.getTargetPath());
        });


        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        _pathsPanel.add(_lblSourcePath, c);
        c.gridx = 1;
        _pathsPanel.add(_btnSourcePath, c);

        c.gridx = 0;
        c.gridy = 1;
        _pathsPanel.add(_lblTargetPath, c);
        c.gridx = 1;
        _pathsPanel.add(_btnTargetPath, c);

        _btnSave.setText("Save");
        _btnSave.setHorizontalAlignment(SwingConstants.CENTER);

        _btnSave.addActionListener(evt->{
            String res = _controller.save();
            JDialog dialog = new JDialog(_frame);
            dialog.setTitle("Sauvegarde");
            JPanel panel = new JPanel();
            JLabel lblResult = new JLabel();
            lblResult.setText(res);
            panel.add(lblResult);
            dialog.setContentPane(panel);
            dialog.setSize(WIDTH / 3, HEIGHT / 3);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });



        _mainPanel.add(_btnSave, BorderLayout.SOUTH);




        _mainPanel.add(_pathsPanel, BorderLayout.CENTER);
        _frame.add(_mainPanel);
        _mainPanel.revalidate();
        _mainPanel.repaint();
    }


    public void setSourcePath(String path) {
        _lblSourcePath.setText(path);
    }

    public void setTargetPath(String path) {
        _lblTargetPath.setText(path);
    }
}
