package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.Socket;

import javax.swing.JFileChooser;

import dao.UserDAO;
import model.Default;
import model.Send;
import view.ConnectDisplay;
import view.MessengerDisplay;
import view.RegisterDisplay;

public class Client extends Default {
	private static Socket client = null;
	private static String name = "";
	private static String roomId = "";

	public static void main(String[] args) {
		try {
			ConnectDisplay connect = new ConnectDisplay();
			MessengerDisplay messenger = new MessengerDisplay();
			RegisterDisplay register = new RegisterDisplay();
			
			connect.createDisplay();
			connect.setDisplay();
			connect.showDisplay();
			
			connect.register.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					connect.closeDisplay();
					register.createDisplay();
					register.setDisplay();
					register.showDisplay();
					
					register.register.addActionListener(new ActionListener() {	
						@Override
						public void actionPerformed(ActionEvent e) {
							if(register.inputUserId.getText().isEmpty() || register.inputUserPassword.getText().isEmpty() || register.inputUserName.getText().isEmpty()) {
								System.out.println("필수값 빠져있음");
								return;
							}
							
							UserDAO dao = UserDAO.getInstance();
							int result = dao.register(register.inputUserId.getText(), register.inputUserPassword.getText(), register.inputUserName.getText());
							
							if(result > 0) {
								System.out.println("회원가입 성공");
								register.closeDisplay();
								connect.showDisplay();
							} else {
								System.out.println("오류 발생");
							}
						}
					});
				}
			});
			
			connect.connect.addActionListener(new ActionListener() {	
				@Override
				public void actionPerformed(ActionEvent e) {
					if(connect.inputIPAdress.getText().isEmpty() || connect.inputRoomNum.getText().isEmpty() || connect.inputUserId.getText().isEmpty()) {
						System.out.println("필수값 빠져있음");
						return;
					}
					
					try {
						client = new Socket(connect.inputIPAdress.getText(), 1724);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					roomId = connect.inputRoomNum.getText();
					name = connect.inputUserId.getText();
					
					Thread thread = new Thread(new Send(client, name + "@join" + roomId));
					thread.start();
					
					connect.closeDisplay();
					messenger.createDisplay();
					messenger.setDisplay();
					messenger.setTitle(roomId);
					messenger.showDisplay();
					messenger.messageField.requestFocus();
					
					Thread receive = new Thread(new ClientReceive(client, messenger));
					receive.start();
				}
			});
			
			messenger.send.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String message = messenger.messageField.getText();
					messenger.messageField.setText("");
					messenger.messageField.requestFocus();
					
					Thread thread = new Thread(new Send(client, name + "@message" + message + "@roomId" + roomId));
					thread.start();
				}
			});
			
			messenger.fileSend.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int select = messenger.chooser.showOpenDialog(null);
					
					if(select == JFileChooser.APPROVE_OPTION) {
						File file = messenger.chooser.getSelectedFile();
						String fileName = file.getAbsolutePath();
						
						Thread thread = new Thread(new Send(client, name + "@file" + fileName + "@roomId" + roomId));
						thread.start();
					} else if(select == JFileChooser.CANCEL_OPTION) {
						System.out.println("취소");
					}
				}
			});
			
			messenger.exitRoom.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("exit");
					Thread thread = new Thread(new Send(client, name + "@exit" + roomId));
					thread.start();
					messenger.closeDisplay();
				}
			});
			
			messenger.viewRoom.addActionListener(new ActionListener() { //방 초대	
				@Override
				public void actionPerformed(ActionEvent e) {
//					String message = messenger.messageField.getText();
//					messenger.messageField.setText("");
//					messenger.messageField.requestFocus();
//					
//					Thread thread = new Thread(new Send(client, message + "@invite" + roomId));
//					thread.start();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}