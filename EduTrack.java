import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;


public class EduTrack extends JFrame {

    private JTextField idField, nameField, gradeField, searchField;
    private JButton addButton, clearButton, saveButton, loadButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private ArrayList<Student2> studentList;

    public EduTrack() {
        studentList = new ArrayList<>();

        setTitle("EduTrack - Student Management System");
        setSize(800, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        addButton = new JButton("Add Student");
        clearButton = new JButton("Clear All");
        saveButton = new JButton("Save Data");
        loadButton = new JButton("Load Data");
        inputPanel.add(addButton);
        inputPanel.add(clearButton);
        inputPanel.add(saveButton);
        inputPanel.add(loadButton);

        add(inputPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

       
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.SOUTH);

        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchStudent();
            }
        });

        
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit Student");
        JMenuItem deleteItem = new JMenuItem("Delete Student");

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        studentTable.setComponentPopupMenu(popupMenu);

        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStudent();
            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });
    }

    
    private void addStudent() {
        String id = idField.getText();
        String name = nameField.getText();
        String grade = gradeField.getText();

        if (!id.isEmpty() && !name.isEmpty() && !grade.isEmpty()) {
            Student2 student = new Student2(id, name, grade);
            studentList.add(student);
            tableModel.addRow(new Object[]{student.getId(), student.getName(), student.getGrade()});
            idField.setText("");
            nameField.setText("");
            gradeField.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Please fill all fields.");
        }
    }

    
    private void clearAll() {
        studentList.clear();
        tableModel.setRowCount(0);  
    }

   
    private void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Rishu.xlsx"))) {
            for (Student2 student : studentList) {
                writer.write(student.getId() + "," + student.getName() + "," + student.getGrade());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Data saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage());
        }
    }

    
    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Rishu.xlsx"))) {
            String line;
            studentList.clear();
            tableModel.setRowCount(0);  
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Student2 student = new Student2(data[0], data[1], data[2]);
                studentList.add(student);
                tableModel.addRow(new Object[]{student.getId(), student.getName(), student.getGrade()});
            }
            JOptionPane.showMessageDialog(this, "Data loaded successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    
    private void searchStudent() {
        String query = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);  
        for (Student2 student : studentList) {
            if (student.getName().toLowerCase().contains(query)) {
                tableModel.addRow(new Object[]{student.getId(), student.getName(), student.getGrade()});
            }
        }
    }

    
    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            String grade = (String) tableModel.getValueAt(selectedRow, 2);

            
            String newName = JOptionPane.showInputDialog("Edit Student Name:", name);
            String newGrade = JOptionPane.showInputDialog("Edit Grade:", grade);

            if (newName != null && newGrade != null) {
                Student2 student = studentList.get(selectedRow);
                student.setName(newName);
                student.setGrade(newGrade);

                tableModel.setValueAt(newName, selectedRow, 1);
                tableModel.setValueAt(newGrade, selectedRow, 2);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.");
        }
    }

    
    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            studentList.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EduTrack().setVisible(true);
            }
        });
    }
}
