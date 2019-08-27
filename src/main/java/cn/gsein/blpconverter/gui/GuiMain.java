package cn.gsein.blpconverter.gui;

import cn.gsein.blpconverter.enums.EnumFilterType;
import cn.gsein.blpconverter.image.BlpConverter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionListener;
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

        jPanel.setLayout(new GridLayout(4, 1));
        JPanel row0 = new JPanel();
        JPanel row1 = new JPanel();
        JPanel row2 = new JPanel();
        JPanel row3 = new JPanel();
        jPanel.add(row0);
        jPanel.add(row1);
        jPanel.add(row2);
        jPanel.add(row3);

        final JTextField input = selectDirectory(row0, "输入文件夹：");

        final JTextField output = selectDirectory(row1, "输出文件夹：");

        BlpConverter converter = new BlpConverter();

        JTextField filterField = new JTextField();
        filterField.setColumns(20);
        JButton filterButton = new JButton("选择");
        filterField.setEnabled(false);
        filterButton.setEnabled(false);
        filterButton.addActionListener(e -> {
            JFileChooser outputDirChooser = new JFileChooser();
            outputDirChooser.setCurrentDirectory(new File("."));
            outputDirChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            outputDirChooser.setAcceptAllFileFilterUsed(false);
            outputDirChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    String name = f.getName();
                    return f.isDirectory() || name.toLowerCase().endsWith(".png");  // 仅显示目录和png文件
                }

                @Override
                public String getDescription() {
                    return "PNG图片(*.png)";
                }
            });
            int value = outputDirChooser.showOpenDialog(outputDirChooser);
            if (value == JFileChooser.APPROVE_OPTION) {
                filterField.setText(outputDirChooser.getSelectedFile().getAbsolutePath());
            }
        });

        ButtonGroup filterRadioGroup = new ButtonGroup();
        addRadioButton(row2, filterRadioGroup, "不使用滤镜", e -> {
            converter.setFilterType(EnumFilterType.NONE);
            filterField.setEnabled(false);
            filterButton.setEnabled(false);
        });

        JRadioButton defaultRadio =  addRadioButton(row2, filterRadioGroup, "默认滤镜", e -> {
            converter.setFilterType(EnumFilterType.DEFAULT);
            filterField.setEnabled(false);
            filterButton.setEnabled(false);
        });
        defaultRadio.setSelected(true);

        addRadioButton(row2, filterRadioGroup, "自定义滤镜", e -> {
            converter.setFilterType(EnumFilterType.SELF_DEFINED);
            filterField.setEnabled(true);
            filterButton.setEnabled(true);
        });
        row2.add(filterField);
        row2.add(filterButton);

        JButton button = new JButton("一键生成");
        row3.add(button);
        button.addActionListener(e -> {
            if (converter.getFilterType() == EnumFilterType.SELF_DEFINED) {
                converter.setFilterFileUrl(filterField.getText());
            }
            try {
                converter.convert(input.getText(), output.getText());
                JOptionPane.showMessageDialog(this, "生成成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, e1.getMessage(), "错误提示", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
        setSize(500, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JRadioButton addRadioButton(JPanel row2, ButtonGroup filterRadioGroup, String name, ActionListener actionListener) {
        JRadioButton filterRadio = new JRadioButton(name);
        filterRadioGroup.add(filterRadio);
        row2.add(filterRadio);
        filterRadio.addActionListener(actionListener);
        return filterRadio;
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
