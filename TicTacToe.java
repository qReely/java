package com.project.myprojects.profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class TicTacToe {
    static final int WAITING_TIME = 1000;
    static JButton[] buttons = new JButton[9];
    static ArrayList<Integer> legalMoves = new ArrayList<>();
    static TicTacToeGUI game;
    static boolean user_turn = true;
    static JLabel ai_score = new JLabel("0"), user_score = new JLabel("0");
    static JLabel turn = new JLabel();
    static String userSymbol = "X";
    static String aiSymbol = "O";

    public static void main(String[] args) {

         game = new TicTacToeGUI();
    }

    public static void alert(String str){
        JOptionPane.showMessageDialog(null, str);
    }

    public static void aiMove(){
        int moveIndex = new Random().nextInt(legalMoves.size());
        int move = legalMoves.get(moveIndex);
        Timer t = new Timer(WAITING_TIME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                legalMoves.remove(moveIndex);
                buttons[move].setText(aiSymbol);
                move();
            }
        });
        t.start();
        t.setRepeats(false);
    }

    public static void move(){
        user_turn = !user_turn;
        if(isEnd()){
            if(user_turn){
                ai_score.setText(Integer.parseInt(ai_score.getText())+1+"");
                alert("You lost! :(");
            }
            else{
                user_score.setText(Integer.parseInt(user_score.getText())+1+"");
                alert("You won! :)");
            }
            clear();
        }
        else if(isDraw()){
            System.out.println("Game ended: Draw!");
            if(!user_turn) move();
            alert("Draw! :|");
            clear();
        }
        else{
            if(!user_turn) aiMove();
            turn.setText(user_turn ? "User's turn" : "AI's turn");
        }
    }
    public static boolean isEnd(){
        if(!buttons[0].getText().equals("") &&  buttons[0].getText().equals(buttons[1].getText()) && buttons[1].getText().equals(buttons[2].getText())) return true;
        if(!buttons[0].getText().equals("") && buttons[0].getText().equals(buttons[3].getText()) && buttons[3].getText().equals(buttons[6].getText())) return true;
        if(!buttons[0].getText().equals("") && buttons[0].getText().equals(buttons[4].getText()) && buttons[4].getText().equals(buttons[8].getText())) return true;
        if(!buttons[3].getText().equals("") && buttons[3].getText().equals(buttons[4].getText()) && buttons[4].getText().equals(buttons[5].getText())) return true;
        if(!buttons[1].getText().equals("") && buttons[1].getText().equals(buttons[4].getText()) && buttons[4].getText().equals(buttons[7].getText())) return true;
        if(!buttons[2].getText().equals("") && buttons[2].getText().equals(buttons[4].getText()) && buttons[4].getText().equals(buttons[6].getText())) return true;
        if(!buttons[6].getText().equals("") && buttons[6].getText().equals(buttons[7].getText()) && buttons[7].getText().equals(buttons[8].getText())) return true;
        return !buttons[2].getText().equals("") && buttons[2].getText().equals(buttons[5].getText()) && buttons[5].getText().equals(buttons[8].getText());
    }
    public static boolean isDraw(){
        for(int i = 0; i < 9; i++){
            if(buttons[i].getText().equals("")) return false;
        }
        return !isEnd();
    }
    public static void clear(){
        legalMoves.clear();
        for(int i = 0; i < 9; i++){
            legalMoves.add(i);
            buttons[i].setText("");
        }
        if(user_turn){
            userSymbol = "X";
            aiSymbol = "O";
        }
        else{
            userSymbol = "O";
            aiSymbol = "X";
            aiMove();
        }
    }
    public static class TicTacToeGUI extends JFrame{

        JPanel title_panel = new JPanel();
        JPanel score_panel = new JPanel();
        JPanel button_panel = new JPanel();
        JLabel title = new JLabel();
        JLabel score = new JLabel();
        JLabel user = new JLabel();
        JLabel ai = new JLabel();

        TicTacToeGUI(){
            super("Tic Tac Toe");

            this.setMinimumSize(new Dimension(595,500));
            this.setSize(595,500);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.getContentPane().setBackground(Color.darkGray);
            this.setLayout(new BorderLayout());

            // Title Panel
            title.setBackground(Color.decode("#e0fbfc"));
            title.setForeground(Color.decode("#98c1d9"));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setVerticalAlignment(SwingConstants.TOP);
            title.setText("Tic Tac Toe");
            title.setFont(new Font("Roboto", Font.BOLD, 45));
            title.setOpaque(true);
            title_panel.setLayout(new BorderLayout());
            title.setBounds(0,0,595,100);
            title_panel.add(title);


            // Buttons Panel
            button_panel.setLayout(new GridLayout(3,3));
            button_panel.setBackground(Color.darkGray);
            button_panel.setSize(300,300);
            button_panel.setMinimumSize(new Dimension(300,300));
            button_panel.setMaximumSize(new Dimension(300,300));

            for(int i = 0; i < 9; i++){
                legalMoves.add(i);
                buttons[i] = new JButton();
                button_panel.add(buttons[i]);
                if(i % 2 != 0) buttons[i].setBackground(Color.decode("#fefae0"));
                else buttons[i].setBackground(Color.decode("#d6ccc2"));
                buttons[i].setFont(new Font("Roboto", Font.BOLD,80));
                buttons[i].setFocusable(false);
                buttons[i].addActionListener(new ButtonListener ());
            }

            // Score Panel
            score_panel.setLayout(new GridBagLayout());
            score_panel.setSize(new Dimension(295,400));
            score_panel.setMaximumSize(new Dimension(295,400));
            score_panel.setMinimumSize(new Dimension(295,400));

            score.setText("Scores");
            score.setFont(new Font("Roboto", Font.BOLD, 55));
            user.setText("User: ");
            user.setFont(new Font("Roboto", Font.BOLD, 35));
            user_score.setFont(new Font("Roboto", Font.BOLD, 35));
            ai.setText("AI: ");
            ai.setFont(new Font("Roboto", Font.BOLD, 35));
            ai_score.setFont(new Font("Roboto", Font.BOLD, 35));
            turn.setText("User's turn");
            turn.setFont(new Font("Roboto", Font.BOLD, 35));

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weighty = 0.5;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 2;
            score_panel.add(score,constraints);
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            score_panel.add(user,constraints);
            constraints.gridx = 1;
            score_panel.add(user_score,constraints);
            constraints.gridx = 0;
            constraints.gridy = 2;
            score_panel.add(ai,constraints);
            constraints.gridx = 1;
            score_panel.add(ai_score,constraints);
            constraints.gridx = 0;
            constraints.gridy = 3;
            constraints.gridwidth = 2;
            score_panel.add(turn,constraints);

            this.add(title_panel, BorderLayout.NORTH);
            this.add(button_panel);
            this.add(score_panel, BorderLayout.EAST);
            this.pack();
            this.setResizable(false);
            this.setVisible(true);
        }
    }

    public static class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i = 0; i < 9; i++){
                if(e.getSource() == buttons[i]){
                    if(buttons[i].getText().equals("") && user_turn) {
                        legalMoves.remove(legalMoves.indexOf(i));
                        if (user_turn) buttons[i].setText(userSymbol);
                        move();
                    }
                }
            }
        }
    }
}
