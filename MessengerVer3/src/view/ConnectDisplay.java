package view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConnectDisplay extends Display {
	private GridLayout layout = new GridLayout(5, 2);
	private JLabel tip1 = new JLabel("접속하려는 서버의 아이피 주소를 입력하세요");
	private JLabel tip2 = new JLabel("접속하려는 방 번호를 입력하세요");
	private JLabel tip3 = new JLabel("사용자 ID를 입력하세요");
	private JLabel tip4 = new JLabel("사용자 비밀번호를 입력하세요");
	public JTextField inputIPAdress = new JTextField("127.0.0.1");//10.10.30.107
	public JTextField inputRoomNum = new JTextField("1");
	public JTextField inputUserId = new JTextField();
	public JTextField inputUserPassword = new JTextField();
	public JButton connect = new JButton("로그인");
	public JButton register = new JButton("회원가입");

	@Override
	public void setDisplay() {
		frame.setPreferredSize(new Dimension(600, 200));
		frame.setLayout(layout);
		frame.add(tip1);
		frame.add(inputIPAdress);
		frame.add(tip2);
		frame.add(inputRoomNum);
		frame.add(tip3);
		frame.add(inputUserId);
		frame.add(tip4);
		frame.add(inputUserPassword);
		frame.add(connect);
		frame.add(register);
	}
	
	public void closeDisplay() {
		frame.setVisible(false);
	}
	
}
