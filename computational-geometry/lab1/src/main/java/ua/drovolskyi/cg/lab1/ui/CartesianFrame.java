package ua.drovolskyi.cg.lab1.ui;



import javax.swing.JFrame;

class Cartesian {
    public static void main(String[] args) {
        CartesianFrame frame = new CartesianFrame();
        frame.showUI();
    }
}

public class CartesianFrame extends JFrame {
    private CartesianPanel panel;

    public CartesianFrame() {
        panel = new CartesianPanel();
        add(panel);
    }

    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Cartesian");
        setSize(700, 700);
        setVisible(true);
    }

    public CartesianPanel getPanel(){
        return panel;
    }
}

/*
    Source: https://stackoverflow.com/questions/30959112/cartesian-plane-in-java
 */
