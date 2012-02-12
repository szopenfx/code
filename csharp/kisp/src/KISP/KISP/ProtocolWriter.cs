using System;
using System.Collections.Generic;
using System.Text;
using System.Net.Sockets;
using System.Net;
using System.IO;
using System.Xml;

namespace KISP.Protocol
{

    class ProtocolWriter : IDisposable
    {
        private TcpClient           ProtocolClient;
        private NetworkStream       ProtocolNetworkStream;
        private StreamWriter        ProtocolStreamWriter;
        private StreamReader        ProtocolStreamReader;

        /// <summary>
        /// Makes the TcpClient with the given parameters
        /// </summary>
        /// <param name="RemoteIP">IP of the remote host</param>
        /// <param name="Port">Port to connect to</param>
        public ProtocolWriter(IPAddress RemoteIP, int Port)
        {
            //try
            //{
                IPEndPoint RemoteIPEndPoint = new IPEndPoint(RemoteIP, Port);
                ProtocolClient = new TcpClient();
                ProtocolClient.Connect(RemoteIPEndPoint);
                if (ProtocolClient.Connected)
                {
                    ProtocolNetworkStream = ProtocolClient.GetStream();
                    ProtocolStreamWriter = new StreamWriter(ProtocolNetworkStream);
                    ProtocolStreamReader = new StreamReader(ProtocolNetworkStream);
                }
                else
                {
                    throw new Exception("Connection to client timed out");
                }
            //}
            //catch
            //{
            //    throw new Exception("Connection to Client failed");
            //}
        }

        /// <summary>
        /// Gives the host the order to prepare the given file at the given port for streamreading
        /// </summary>
        /// <param name="FileName">The file that the server has to send</param>
        /// <param name="Port">The port on which the server has to prepare the stream</param>
        /// <param name="FileLength">Lenght of the file to get in bytes</param>
        /// <returns>True if succeeded, false if not</returns>
        public bool Get(string FileName, int Port, long FileLength)
        {
            try
            {
                //FileInfo DownloadFileInfo = new FileInfo(FileName);

                ProtocolStreamWriter.WriteLine("get " + Port + " " + FileLength + " " + FileName);
                ProtocolStreamWriter.Flush();

                if (ProtocolStreamReader.ReadLine() == "ok get")
                {
                    //this.Dispose();
                    return true;
                }
                else
                {
                    //this.Dispose();
                    return false;
                }
            }
            //catch
            //{
            //    throw new Exception("Error Sending the Get file command to peer");
            //}
            finally
            {
                Dispose();
            }
            
        }


        /// <summary>
        /// Gives the server the order to search for files with the given SearchString in the names
        /// </summary>
        /// <param name="SearchString">The searchstring</param>
        /// <returns>XmlDocument containg the search results, empty XmlDocument if there was a connection error</returns>
        public XmlDocument Search(string SearchString)
        {
            try
            {
                ProtocolStreamWriter.WriteLine("search " + SearchString);
                ProtocolStreamWriter.Flush();
                XmlDocument TmpDocument = new XmlDocument();
                TmpDocument.LoadXml(ProtocolStreamReader.ReadLine());
                return TmpDocument;

            }
            //catch
            //{
            //    throw new Exception("Error performing search on peer");
            //}
            finally
            {
                Dispose();
            }
        }

        /// <summary>
        /// Orders the server to identify itself
        /// </summary>
        /// <returns>IdentifyObject containing remote IP and Name</returns>
        public IdentifyObject Identify()
        {
            string Response = string.Empty;
            try
            {
                ProtocolStreamWriter.WriteLine("identify");
                ProtocolStreamWriter.Flush();

                Response = ProtocolStreamReader.ReadLine();

                char[] Delimiter = new char[1];
                Delimiter[0] = ' ';
                string[] Params = Response.Split(Delimiter);

                return new IdentifyObject(Params[1], Params[0]);


            }
            catch
            {
                throw new Exception("Error getting identification");
            }
            finally
            {
                Dispose();
            }
        }


        /// <summary>
        /// Sends the given chat message to the server
        /// </summary>
        /// <param name="Visibility">public or private</param>
        /// <param name="FromUsername">The username that send this message</param>
        /// <param name="Message">The message itself</param>
        /// <returns>true if the message was succesfully send, false if it failed</returns>
        public bool Chat(string Visibility, string FromUsername, string Message)
        {
            try
            {
                ProtocolStreamWriter.WriteLine("chat " + Visibility + " " + FromUsername + " " + Message);
                ProtocolStreamWriter.Flush();
                return true;
            }
            //catch
            //{
            //    throw new Exception("Error sending chat message to peer");
            //}
            finally
            {
                Dispose();
            }
        }

        /// <summary>
        /// Orders the server to iniate a game at the given port
        /// </summary>
        /// <param name="FromUsername">The username that wants to play the game</param>
        /// <param name="Port">The port on which this client wants the server to iniate the game</param>
        /// <returns>string with the remote users name</returns>
        public void InitGame(string FromUsername, int Port)
        {
            try
            {
                ProtocolStreamWriter.WriteLine("initgame " + FromUsername + " " + Port);
                ProtocolStreamWriter.Flush();
            }
            finally
            {
                Dispose();
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="UserName"></param>
        /// <param name="Port"></param>
        public void AcceptGame(string UserName, int Port)
        {
            try
            {
                ProtocolStreamWriter.WriteLine("acceptgame " + UserName + " " + Port.ToString());
                ProtocolStreamWriter.Flush();
            }
            finally
            {
                Dispose();
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="UserName"></param>
        /// <param name="Port"></param>
        public void RejectGame(string UserName, int Port)
        {
            try
            {
                ProtocolStreamWriter.WriteLine("rejectgame " + UserName + " " + Port.ToString());
                ProtocolStreamWriter.Flush();
            }
            finally
            {
                Dispose();
            }
        }


        /// <summary>
        /// Free's the resources for this object and closes all the network streams. iDisposable implementation
        /// </summary>
        public void Dispose()
        {
            ProtocolClient.Close();
            ProtocolNetworkStream.Close();
            ProtocolStreamReader.Close();
            ProtocolStreamWriter.Close();
        }



        /// <summary>
        /// Object used for peer identification
        /// </summary>
        public class IdentifyObject
        {
            private IPAddress   _IP;
            private string      _Name;

            /// <summary>
            /// Sets the Name and IP
            /// </summary>
            /// <param name="IP">IP of the host</param>
            /// <param name="Name">Name of the host</param>
            public IdentifyObject(string IP, string Name)
            {
                _IP = IPAddress.Parse(IP);
                _Name = Name;
            }

            /// <summary>
            /// Gets the IP of the host
            /// </summary>
            public IPAddress IP
            {
                get
                {
                    return _IP;
                }
            }

            /// <summary>
            /// Gets the Name of the host
            /// </summary>
            public string Name
            {
                get
                {
                    return _Name;
                }
            }

        }
    }
}
