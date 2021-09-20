package Telinha;
//importações
import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor{
    ArrayList saidaTela;
    public class Client implements Runnable {
       private BufferedReader leitura;
       private Socket socket;
        
        public Client(Socket clientSOcket) {
            try {
            	socket = clientSOcket;
                InputStreamReader leituraok = new InputStreamReader(socket.getInputStream());//Faz a leitura de dados do socket
                leitura = new BufferedReader(leituraok); //faz leitura dos arquivos
            } catch (Exception ex){ 
            	ex.printStackTrace(); 
            }
        }
        
        public void run() {
            String mensagem;
            try {
                while ((mensagem = leitura.readLine()) != null) {//Verifica se o campo é nulo
                    mensagemUsuarios(mensagem);
                }
            } 	catch(Exception ex){ 
            	ex.printStackTrace(); 
            }
        }
        
        public void mensagemUsuarios(String mensagem) {
            Iterator tratadordemensagem = saidaTela.iterator();
            while (tratadordemensagem.hasNext()) {
                try {
                    PrintWriter escrever = (PrintWriter) tratadordemensagem.next();//Escreve para dentro do arquivo
                    escrever.println(mensagem);
                    escrever.flush();//Limpa os dados da memória
                	} catch (Exception ex) { 
                		ex.printStackTrace(); }
            	}
        }
    }
    
    public static void main(String[] args) {
        new Servidor().go();

    }
    
    public void go() {
    	saidaTela = new ArrayList();
        try {
            ServerSocket socketdoserver = new ServerSocket(6969);
            while(true) {
                Socket socketdoclient = socketdoserver.accept();
                PrintWriter escrever = new PrintWriter(socketdoclient.getOutputStream());
                saidaTela.add(escrever);
                Thread threadServidor = new Thread(new Client(socketdoclient));
                threadServidor.start();
            }
        } catch (Exception ex) { 
        	ex.printStackTrace(); 
        }
    }
}