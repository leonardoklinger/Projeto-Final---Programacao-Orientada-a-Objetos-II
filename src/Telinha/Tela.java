package Telinha;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class Tela extends JFrame{//minha JFrame
	//Importações
	private JTextField mensagem;
	private JButton enviar;
	private JLabel chatText;
	private static JTextArea chat;
	private JScrollPane barra;
	private static BufferedReader ler;
    private PrintWriter escrever;
    private Socket socket;
	public Tela() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setBounds(10, 10, 620, 700);//Tamanho da minha tela
		this.getContentPane().setBackground(new Color(147,0,255));
		setTitle("Chat Socket");//O titulo da tela
		
		barra = new JScrollPane();//o scroll da JtextArea
		barra.setVerticalScrollBarPolicy(barra.VERTICAL_SCROLLBAR_ALWAYS);//o scroll da JtextArea
		chat = new JTextArea();
		chat.setBounds(10, 80, 470, 470);//Construção da minha JTextArea
		chat.setEditable(false);
		chat.setBackground(new Color(142,28,227));
		chat.setFont(new Font("Arial", 1, 18));
		chat.setBorder(null);
		
		chatText = new JLabel("Chat");
		chatText.setBounds(10, 30, 100, 50);//Construção do meu JLabel
		chatText.setFont(new Font("Arial", 1, 18));
		chatText.setForeground(new Color(255,255,255));
		this.add(chatText);
		
		barra.setBounds(10, 80, 470, 470);//Setando as coordenadas do JScrollPane
		barra.getViewport().setBackground(Color.white);//Setando a sua cor para branco
		barra.getViewport().add(chat);//Juntando o meu JScrollPane com o Jtextarea
		barra.setBorder(null);
		this.add(barra);
		
		mensagem = new JTextField();
		mensagem.setBackground(new Color(142,28,227));
		mensagem.setForeground(Color.white);
		mensagem.setFont(new Font("Arial", 1, 15));
		mensagem.setBounds(10, 560, 470, 50);//Minha barra de mensagem
		mensagem.setBorder(null);
		this.add(mensagem);
		
		enviar = new JButton("Enviar");
		enviar.setForeground(Color.white);
		enviar.setFont(new Font("Arial", 1, 18));
		enviar.setBackground(new Color(142,28,227));
		enviar.setBounds(490, 560, 100, 50);//Meu botão de enviar
		enviar.setBorder(null);
		this.add(enviar);
		
        try {
        	socket = new Socket("localhost", 6969);//O ip do meu computador + a porta !
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());//Faz a leitura de dados do socket
            ler = new BufferedReader(streamReader);//faz leitura dos arquivos
            escrever = new PrintWriter(socket.getOutputStream());//Escreve para dentro do socket
        		} catch(IOException ex) {
        		ex.printStackTrace();
        	}
		enviar.addActionListener(new ActionListener() {
			String nome = JOptionPane.showInputDialog("Digite seu nome: ");//Mensagem de texto
		    public void actionPerformed(ActionEvent e) {
		    	if(mensagem.getText().equals("")) {//Verifica se o campo da mensagem está vazia
		    		JOptionPane.showMessageDialog(null,"Campo da mensagem está vazia !","Erro",JOptionPane.INFORMATION_MESSAGE);
		    	}else {
		            try {
		                escrever.println(nome + ": " + mensagem.getText());//Captura o teclado e escreve dentro do arquivo com quebra de linha
		                escrever.flush();//Limpa os dados da memória
		            }
		            catch (Exception ex) {
		                ex.printStackTrace();
		            }
		            	mensagem.setText("");//Limpa o campo da mensagem
		            	mensagem.requestFocus();//Foca a mensagem
		    	}
		    }
		});
	}
	

	public static void main(String[] args) {
		Tela tela = new Tela();
		tela.setVisible(true);
        Thread threadTela = new Thread(new Runnable() { //cria uma thread
            public void run() {
        String message;
        try {
            while ((message = ler.readLine()) != null) {
            	chat.setForeground(Color.white);
                chat.append(message + "\n");//Printa para dentro do JTextArea
            }
        		}catch(IOException ex){
        			ex.printStackTrace();
        		}
            }
        });
        threadTela.start();//Starta a thread
	}
}