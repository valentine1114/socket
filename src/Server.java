import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Socket socket;

    private static DataInputStream dis;

    private static DataOutputStream dos;

    public static void main(String[] args) throws Exception {
        //创建服务
        ServerSocket ss = new ServerSocket(8821);
        // 创建接收客户端socket对象
        //Socket socket;
        System.out.println("等待客户端连接...");
        //循环监听等待客户端的连接
        socket = ss.accept();
        System.out.println("成功连接");

        //发送文件
        sendFile(socket);
        getMessage(socket);
        //关闭资源
        dos.close();
        dis.close();
        socket.close();
        ss.close();

    }

    /**
     * 接收客户端询问的文件名字，如果存在发送文件内容给客户端，不存在显示"文件不存在"
     *
     * @param socket
     * @throws IOException
     */

    private static void sendFile(Socket socket) throws IOException {
        try {
            //输入流
            dis = new DataInputStream(socket.getInputStream());
            //输出流
            dos = new DataOutputStream(socket.getOutputStream());
            //读取文件名
            String fileName = dis.readUTF();
            //寻找文件地址
            File directory = new File("src/ServerFiles");
            File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
            //判断文件是否存在
            if (!file.exists()) {
                System.out.println("文件不存在！");
                dos.writeUTF("文件不存在");
                System.exit(0);
            } else System.out.println("开始传输");

            dis = new DataInputStream(new FileInputStream(file));
            //文件名字和文件长度
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();

            //文件输出

            byte[] buffer = new byte[1024];
            int len;

            while ((len = dis.read(buffer, 0, buffer.length)) != -1) {
                dos.write(buffer, 0, len);
                dos.flush();
            }

            System.out.println("传输完成");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否获得客户端接收文件消息方法
     *
     * @param socket
     * @throws Exception
     */

    public static void getMessage(Socket socket) throws Exception {
        dis = new DataInputStream(socket.getInputStream());
        if (dis.readUTF().equals("received")) {
            System.out.println("客户端已发送确认");
        }
    }
}
