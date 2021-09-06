package view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class RegisterDisplay extends Display {
	private GridLayout layout = new GridLayout(4, 2);
	private JLabel tip1 = new JLabel("ID를 입력해주세요");
	private JLabel tip2 = new JLabel("비밀번호를 입력해주세요");
	private JLabel tip3 = new JLabel("이름을 입력해주세요");
	public JTextField inputUserId = new JTextField();// 10.10.30.107
	public JTextField inputUserPassword = new JTextField();
	public JTextField inputUserName = new JTextField();
	public JButton register = new JButton("회원가입");

	@Override
	public void setDisplay() {
		frame.setPreferredSize(new Dimension(600, 200));
		frame.setLayout(layout);
		frame.add(tip1);
		frame.add(inputUserId);
		frame.add(tip2);
		frame.add(inputUserPassword);
		frame.add(tip3);
		frame.add(inputUserName);
		frame.add(register);
	}

	public void closeDisplay() {
		frame.setVisible(false);
	}
}
