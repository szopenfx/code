using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using KISP.Interface.Graphical;
using KISP.Nexus;

namespace KISP.Binary
{
    /// <summary>
    /// Listens on a local port and sends a local binary file on connection
    /// </summary>
    public class BinaryServer
    {
        private int Port;
        private string FileName;
        private GraphicalInterface Interface;
        private Nexus.Nexus MainNexus;

        /// <summary>
        /// Sets the local variables
        /// </summary>
        /// <param name="_Port">Port to listen to</param>
        /// <param name="_FileName">Local resource to be send</param>
        /// <param name="_Interface">Pointer to the interface (for status updating)</param>
        /// <param name="_Nexus">Pointer to Nexus</param>
        public BinaryServer(int _Port, string _FileName, GraphicalInterface _Interface, Nexus.Nexus _Nexus)
        {
            Port = _Port;
            FileName = _FileName;
            Interface = _Interface;
            MainNexus = _Nexus;
        }

        /// <summary>
        /// Starts the binary server and waits for connections. If connected, the
        /// server sends the raw binary data over the socket
        /// </summary>
        public void Start()
        {
            IncUploadDelegate iud =
             new IncUploadDelegate(Interface.IncUploads);
            Interface.Invoke(iud);

            Console.WriteLine("Server Send Begins");
            //Interface.IncUploads();
            TcpListener TestListener = new TcpListener(Port);
            TestListener.Start();

            TcpClient TestClient = TestListener.AcceptTcpClient();

            NetworkStream TestNetworkStream = TestClient.GetStream();
     

            BinaryWriter TestWriter = new BinaryWriter(TestNetworkStream);


            FileInfo _FileInfo = new FileInfo(FileName);

            //StreamReader TestReader = new StreamReader(_FileInfo.OpenRead());

            BinaryReader TestReader = new BinaryReader(_FileInfo.OpenRead());



            byte[] Buff = new byte[1024];

            long bytessend = 0;
           
            while (bytessend < _FileInfo.Length)
            {
                TestReader.Read(Buff, 0, Buff.Length);
                if ((bytessend + 1024) > _FileInfo.Length)
                    TestWriter.Write(Buff, 0, Convert.ToInt32(_FileInfo.Length - bytessend));
                else
                    TestWriter.Write(Buff, 0, Buff.Length);
                

                bytessend = bytessend + 1024;

                
                //Interface.Progress1KinFileUpLoad(FileName);
            }
            //TestWriter.Flush();

            DecUploadDelegate dud =
                    new DecUploadDelegate(Interface.DecUploads);
            Interface.Invoke(dud);

            TestNetworkStream.Close();
            TestWriter.Close();
            TestReader.Close();
            TestClient.Close();
            TestListener.Stop();
            MainNexus.DeleteDownloadPort(Port);
            
            
        }
    }
}
