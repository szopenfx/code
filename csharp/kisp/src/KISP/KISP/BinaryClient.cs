using System;
using System.IO;
using System.Threading;
using System.Net;
using System.Net.Sockets;

using KISP.Interface.Graphical;

namespace KISP.Binary
{
    /// <summary>
    /// Binary Socket client that get binary data from a server socket and creates
    /// a new file.
    /// </summary>
    public class BinaryClient
    {
        private string FileName;
        private string ServerIP;
        private int Port;
        private long FileLength;
        private Nexus.Nexus Nexus;


        /// <summary>
        /// Sets the necessary variables
        /// </summary>
        /// <param name="_ServerIP">IP of the server to connect to</param>
        /// <param name="_Port">Port of the server to connect to</param>
        /// <param name="_FileName">Filename where the data has to be saved</param>
        /// <param name="_Nexus">Pointer to the central Nexus</param>
        /// <param name="_FileLength">The raw binary file lengt of the file to be received</param>
        public BinaryClient(string _ServerIP, int _Port, string _FileName, Nexus.Nexus _Nexus, long _FileLength)
        {
            FileName = _FileName;
            Port = _Port;
            ServerIP = _ServerIP;
            Nexus = _Nexus;
            FileLength = _FileLength;
        }


        /// <summary>
        /// Connects to the server and creates the local resource
        /// </summary>
        public void ReceiveFile()
        {
            Thread.Sleep(1000);
            Console.WriteLine("Client Receiving Started");

            TcpClient TestClient = new TcpClient();
            IPEndPoint ClientIPEndPoint = new IPEndPoint(IPAddress.Parse(ServerIP), Port);
            TestClient.Connect(ClientIPEndPoint);

            NetworkStream TestStream = TestClient.GetStream();
            //StreamReader TestReader = new StreamReader(TestStream);

            BinaryReader TestReader = new BinaryReader(TestStream);

            FileStream TestFileStream = new FileStream(FileName, FileMode.Create);

            BinaryWriter TestWriter = new BinaryWriter(TestFileStream);

            //char[] CharrBuff = new char[1024];

            byte[] Buffer = new byte[1024];
            int ByteCounter = 0;
            long BytesReceived = 0;
            //while (!(Nexus._Interface.DownloadCompleted(FileName)))
            DownloadProgressDelegate dpd =
                            new DownloadProgressDelegate(Nexus._Interface.Progress1KinFileDownload);
            while (BytesReceived < FileLength)
            {
                try
                {
                  
                    TestReader.Read(Buffer, 0, Buffer.Length);
                    TestWriter.Write(Buffer, 0, Buffer.Length);
                    BytesReceived = BytesReceived + Buffer.Length;
                    //TestWriter.Flush();
                    ByteCounter++;
                    if (ByteCounter == 100)
                    {
                        
                        Nexus._Interface.Invoke(dpd, new Object[] { FileName });
                        ByteCounter = 0;
                    }
                    
                }
                catch (Exception e)
                {
                    Console.WriteLine("IN CLIENT CATCH!");
                    Console.WriteLine(e);
                    TestWriter.Write(Buffer, 0, Buffer.Length);
                    TestWriter.Flush();
                    
                    Nexus._Interface.Invoke(dpd, new Object[] { FileName });
                    break;
                }
            }

            Nexus._Interface.Invoke(dpd, new Object[] { FileName });
            TestClient.Close();
            TestReader.Close();
            TestFileStream.Close();
            TestWriter.Close();
            Console.WriteLine("File Received!");
        }
    }
}

