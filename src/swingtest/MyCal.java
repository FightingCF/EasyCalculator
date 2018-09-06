package swingtest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

public class MyCal extends JFrame{
	
	private final String[] KEYS = {"%", "√", "x²", "1/x", "7", "8", "9", "*", "4", "5", "6", "/", "1", "2", "3", "+", ".", "0", "=", "-"};
	private JButton delete;
	private JButton[] btn = new JButton[KEYS.length];
	private JPanel panel1, panel2;
	private JTextField tx;
	private Double result = 0.0;
	private boolean first = true;		
	private boolean error = false;
	private Queue<String> opr = new LinkedList<String>();
	
	public MyCal() {
		
		panel1 = new JPanel(new GridLayout(5,4,3,3));
		panel2 = new JPanel();
		
		NumberListener lis1 = new NumberListener();
		OperatorListener lis2 = new OperatorListener();
		this.addKeyListener(lis1);
		this.addKeyListener(lis2);
		
		tx = new JTextField(String.valueOf(result),20);
		tx.setHorizontalAlignment(JTextField.RIGHT);	
		tx.setEditable(false);
		tx.setFocusable(false);
		
		for (int i = 0; i < KEYS.length; i++) {
			btn[i] = new JButton(KEYS[i]);			
			if ("0123456789.".indexOf(KEYS[i]) >= 0) {
				btn[i].addActionListener(lis1);
			}
			else {
				btn[i].addActionListener(lis2);
			}
			panel1.add(btn[i]);
		}
		
		
		this.delete = new JButton("清空");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				result = 0.0;
				tx.setText("0.0");
				first = true;		
				MyCal.this.requestFocus();
			}
		});
		delete.addKeyListener(lis1);
		
		panel2.add(tx);
		panel2.add(delete);

		this.setTitle("计算器");
		this.setSize(300, 400);
		this.setLocationRelativeTo(null);	
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		Box box = Box.createVerticalBox();
		box.add(panel2);
		box.add(panel1);
		
		this.setVisible(true);
		this.setContentPane(box);
		this.setResizable(false);
		pack();
	}
	private class NumberListener extends KeyAdapter implements ActionListener{
		public void actionPerformed(ActionEvent e){ 
			String key = e.getActionCommand();
			handleNumber(key);	
			MyCal.this.requestFocus();
		}
		public void keyPressed(KeyEvent e) {
			char key = e.getKeyChar();
			String text = tx.getText();
			if (key >= '0' && key <= '9' || key == '.') {
				handleNumber(String.valueOf(key));	
			}
			else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
				if (!text.equals(""))
					tx.setText(text.substring(0, text.length()-1));
		}
	}
	private class OperatorListener extends KeyAdapter implements ActionListener{
		public void actionPerformed(ActionEvent e){ 
			String s = e.getActionCommand();
			if (s.equals("√")) 
				result = Math.sqrt(getNum());
			else if (s.equals("x²"))
				result = Math.pow(getNum(), 2);
			else if (s.equals("1/x")) {
				if (getNum() == 0.0) {
					error = true;
					tx.setText("运算错误！");
				}
				else
					result = 1 / getNum();
			}
			else {
				opr.offer(s);
				if (opr.size() == 2) {	
					handleTwiceOperator(opr.remove());
				}
				else {
					result = getNum();
					first = true;	
					tx.setText(String.valueOf(result));
				}
			}
			if (!error) 
				tx.setText(String.valueOf(result));
			first = true;
			error = false;
			MyCal.this.requestFocus();
		}
		public void keyPressed(KeyEvent e) {
			char key = e.getKeyChar();
			if (key == '+' || key == '-' || key == '*' || key == '/' || key == '=') {
				opr.offer(String.valueOf(key));
				if (opr.size() == 2) {	
					handleTwiceOperator(opr.remove());
				}
				else {
					result = getNum();
					first = true;	
					tx.setText(String.valueOf(result));
				}
				if (!error) 
					tx.setText(String.valueOf(result));
				first = true;
				error = false;
			}
		}
	}
	private void handleNumber(String s) {
		if (first)
			tx.setText(s);
		else if (s.equals(".") && !tx.getText().contains("."))
			tx.setText(tx.getText() + '.');
		else if (!s.equals("."))
			tx.setText(tx.getText() + s);
		first = false;		
		MyCal.this.requestFocus();
	}
	private void handleTwiceOperator(String s) {
		if (s.equals("/")) {
			if (getNum() == 0.0) {
				error = true;
				tx.setText("运算错误！");
			}
			else
				result /= getNum();
		}
		else if (s.equals("*"))
			result *= getNum();
		else if (s.equals("+"))
			result += getNum();
		else if (s.equals("-"))
			result -= getNum();
		else if (s.equals("%")) 
			result %= getNum();

		else if (s.equals("=")) {
			result = getNum();
		}
	}
	private double getNum() {
		double t = 0.0;
		t = Double.valueOf(tx.getText()).doubleValue();
		return t;
	}
	public static void main(String[] args) {
		MyCal calculator = new MyCal();
	}

}

/*class NumberButton extends JButton{
	private JButton number;
	public NumberButton(String s, JTextField tx) {
		number = new JButton(s);
		number.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text = tx.getText();
				tx.setText(text + s);
				
			}
		});
	}
	public JButton getButton() {
		return number;
	}
}
*/
