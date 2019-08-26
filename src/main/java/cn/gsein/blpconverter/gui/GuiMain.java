package cn.gsein.blpconverter.gui;

import cn.gsein.blpconverter.image.BlpConverter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author G.Seinfeld
 * @date 2019/08/26
 */
public class GuiMain extends JFrame {

    private static final String WINDOWS = "win";
    private static final String WINDOWS_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

    private GuiMain() throws HeadlessException {

        JPanel jPanel = new JPanel();
        setContentPane(jPanel);

        jPanel.setLayout(new GridLayout(3, 1));
        JPanel row0 = new JPanel();
        JPanel row1 = new JPanel();
        JPanel row2 = new JPanel();
        jPanel.add(row0);
        jPanel.add(row1);
        jPanel.add(row2);

        final JTextField input = selectDirectory(row0, "输入文件夹：");

        final JTextField output = selectDirectory(row1, "输出文件夹：");

        JButton button = new JButton("一键生成");
        row2.add(button);
        button.addActionListener(e -> {
            BlpConverter converter = new BlpConverter();
            try {
                converter.convert(input.getText(), output.getText());
                JOptionPane.showMessageDialog(this, "生成成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, e1.getMessage(), "错误提示", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
        setSize(500, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JTextField selectDirectory(JPanel jPanel, String s) {
        JLabel outputLabel = new JLabel(s);
        jPanel.add(outputLabel);

        JTextField outputField = new JTextField();
        outputField.setColumns(50);
        jPanel.add(outputField);

        JButton outputButton = new JButton("选择");
        jPanel.add(outputButton);
        outputButton.addActionListener(e -> {
            JFileChooser outputDirChooser = new JFileChooser();
            outputDirChooser.setCurrentDirectory(new File("."));
            outputDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int value = outputDirChooser.showOpenDialog(outputDirChooser);
            if (value == JFileChooser.APPROVE_OPTION) {
                outputField.setText(outputDirChooser.getSelectedFile().getAbsolutePath());
            }
        });
        return outputField;
    }


    public static void main(String[] args) {

        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(WINDOWS)) {
            try {
                UIManager.setLookAndFeel(WINDOWS_FEEL);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }
        new GuiMain();


    }
}
