import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket socket;

    private static DataInputStream dis;

    private static DataOutputStream dos;

    public static void main(String[] args) throws Exception {
        //// 创建客户端socket 对象
       socket = new Socket("127.0.0.1", 8821);
        System.out.println("已发起服务器连接");
        //接收用户输入的需要寻找的文件名
        System.out.println("请输入请求的文件名：");
        Scanner in = new Scanner(System.in);
        String fileName = in.nextLine();
        //发送文件名给服务器，请求传输文件内容
        sendMessage(fileName, socket);
        System.out.println("已发送文件名");
        //接收服务器传输的文件内容
        getFile(socket);

        //关闭资源
        // ！！！注意io流关闭的同时socket也关闭了，所以io流放在最后关闭
        dis.close();
        if (dos != null) {
            dos.close();
        }
        socket.close();
    }

    /**
     * 向服务器发送文字信息
     */
    public static void sendMessage(String str, Socket socket) {

        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(str);
            dos.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取服务器传输字节流，并打印到文件中
     */
    public static void getFile(Socket socket) throws Exception {

        try {//读取文件流


            dis = new DataInputStream(socket.getInputStream());
            String fileName=dis.readUTF();
            long fileLen=0;
            //System.out.println(dis.readUTF());
            //判断文件输入流是否有内容
            if (fileName.equals("文件不存在")) {
                System.out.println("文件不存在！");
                System.exit(0);
            }
            else{
                //文件名字和文件长度
                fileLen = dis.readLong();
            }

            //创建文件传输地址
            File directory = new File("src/ClientFiles");

            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);//文件传输地址
            if (!file.exists()) {
                file.createNewFile();
            }


            byte[] buffer = new byte[1024];
            int len = 0;

            //输出文件
            dos = new DataOutputStream(new FileOutputStream(file));
            while ((len = dis.read(buffer, 0, buffer.length)) != -1) {
                dos.write(buffer, 0, len);
                dos.flush();

                //只要服务器不把输出流关闭，客户端一直在循环读入，无法发送确认收到的信息
                // 传输文件时把文件大小一起进行传输，客户端自行判断当前收到的文件是否接收成功
                //！！！不要放在循环外确认
                if (file.length() == fileLen) {
                    System.out.println("文件接收完成");
                    String message = "received";
                    sendMessage(message, socket);
                    System.out.println("已发送确认信息");

                }

            }


        } catch (Exception e) {
            throw new RuntimeException(e);

        }


    }

}
