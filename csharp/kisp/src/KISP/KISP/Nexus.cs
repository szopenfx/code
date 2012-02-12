using System;
using System.Collections;
using System.Text;
using System.Threading;
using System.IO;
using System.Net;
using System.Xml;

using KISP.Protocol;
using KISP.Binary;
using KISP.Interface.Graphical;
using KISP.Chess;
using KISP.Multicast;

namespace KISP.Nexus
{
    /// <summary>
    /// Exception for dealing with KISP errors
    /// </summary>
    public class BullshitException : Exception
    {
        /// <summary>
        /// Construct the nice bullshit exception
        /// </summary>
        /// <param name="message"></param>
        public BullshitException(string message)
            : base(message)
        {
        }
    }

    /// <summary>
    /// Central Class of the program
    /// </summary>
    public class Nexus
    {
        private PeerList _Peers;
        private Protocol.Protocol _Protocol;
        private Search.Search _Search;
        private Multicast.Multicast _Multicast;
        private int _ChessPort = Configuration.ChessPort();
        private ArrayList _UsedPorts = new ArrayList();

        /// <summary>
        /// Delegate for adding a peer to the form and the peer list
        /// </summary>
        /// <param name="IPAddress">IP address of peer</param>
        public delegate void AddPeerDelegate(string IPAddress);

        /// <summary>
        /// Reference to graphical interface
        /// </summary>
        public GraphicalInterface _Interface;

        /// <summary>
        /// Construct the nexus
        /// </summary>
        public Nexus()
        {
            _Protocol = new Protocol.Protocol(Configuration.ProtocolPort(), this);
            _Search = new Search.Search();
            _Peers = new PeerList();
            _Multicast = new Multicast.Multicast(this, true, true);

            _UsedPorts.Add(8000);
        }

        /// <summary>
        /// Invoke delegate in GUI thread
        /// </summary>
        /// <param name="m">Delegate</param>
        public void Invoke(Delegate d)
        {
            if(_Interface != null)
                _Interface.Invoke(d);
        }

        /// <summary>
        /// Invoke delegate in GUI thread
        /// </summary>
        /// <param name="d">Delegate</param>
        /// <param name="args">Parameters</param>
        public void Invoke(Delegate d, params object[] args)
        {
            if(_Interface != null)
                _Interface.Invoke(d, args);
        }
        
        /// <summary>
        /// Download the given file from the given peer. Start a binaryserver on which to receive the file
        /// </summary>
        /// <param name="PeerIP">IP Address of the peer</param>
        /// <param name="LocalPath">Path where the remote peer can find the file</param>
        /// <param name="Port">Port on which to start the binaryserver</param>
        public void SendFile(IPAddress PeerIP, string LocalPath, int Port)
        {
            try
            {

                new Thread(new ThreadStart(new BinaryServer(Port, LocalPath, _Interface, this).Start)).Start();
                Console.WriteLine("Binary server thread started!!");
                string ServerIP = PeerIP.ToString();
                //DisplayUploadDelegate dud =
                //                   new DisplayUploadDelegate(_Interface.DisplayFileUpload);
                //_Interface.Invoke(dud, new Object[] { LocalPath, ServerIP });
               
            }
            catch (Exception e)
            {
                throw new BullshitException("File send failed - File:" + LocalPath + " " + e);
            }
        }

        /// <summary>
        /// Check if the given port is available
        /// </summary>
        /// <param name="Port">Port number</param>
        /// <returns>True if it is available, false if not</returns>
        public bool IsPortAvailable(int Port)
        {
            return ((int)_UsedPorts[_UsedPorts.Count - 1]) <= Port;
        }

        /// <summary>
        /// Returns a new available download port
        /// </summary>
        /// <returns>New available download port</returns>
        public int GetNewDownloadPort()
        {
            _UsedPorts.Add((((int)_UsedPorts[_UsedPorts.Count - 1]) + 1));
            return ((int)_UsedPorts[_UsedPorts.Count - 1]);
        }

        /// <summary>
        /// Delet a download port after it has been used
        /// </summary>
        /// <param name="Number">The port to be deleted</param>
        public void DeleteDownloadPort(int Number)
        {
            for (int i = 0; i < _UsedPorts.Count; i++ )
            {
                if ((int)_UsedPorts[i] == Number)
                {
                    _UsedPorts.RemoveAt(i);
                    return;
                }
            }
        }

        /// <summary>
        /// Performs a search on peers, and displays the data
        /// </summary>
        /// <param name="SearchString">The string to be searched for</param>
        public void PerformSearchOnPeers(string SearchString)
        {
            foreach (Peer CurrentPeer in _Peers.Peers)
            {
                Console.WriteLine(CurrentPeer.UserName);
                XmlDocument PeerSearchResult = new ProtocolWriter(CurrentPeer.IPAddr, Configuration.ProtocolPort()).Search(SearchString);

                foreach (XmlNode SearchNode in PeerSearchResult.LastChild.ChildNodes)
                {
                    string Source = "(unknown)";
                    string Size = "(unknown)";
                    string Type = "(unknown)";
                    string Name = "(unknown)";

                    foreach (XmlNode ItemNode in SearchNode.ChildNodes)
                    {
                        

                        switch (ItemNode.LocalName)
                        {
                            case "source":
                                {
                                    Source = ItemNode.InnerText;
                                    break;
                                }
                            case "filename":
                                {
                                    Name = ItemNode.InnerText;
                                    break;
                                }
                            case "size":
                                {
                                    Console.WriteLine(ItemNode.InnerText);
                                    Size = ItemNode.InnerText;
                                    break;
                                }
                            case "filetype":
                                {
                                    Type = ItemNode.InnerText;
                                    break;
                                }
                        }
                    }
                    _Interface.DisplaySearchResult(Name, Convert.ToInt64(Size), CurrentPeer.UserName, CurrentPeer.IPAddr.ToString(), Source, Type);    
                }
                    
            }
        }

        /// <summary>
        /// Send a file to a given peer
        /// </summary>
        /// <param name="PeerIP">Address of the peer</param>
        /// <param name="RemotePath">Path</param>
        /// <param name="Port">Port</param>
        /// <param name="FileLength">Length of file</param>
        public void GetFile(IPAddress PeerIP, string RemotePath, int Port, long FileLength)
        {
            if (new ProtocolWriter(PeerIP, Configuration.ProtocolPort()).Get(RemotePath, Port, FileLength))
            {
                RemotePath = Configuration.DownloadDir() + "\\" + Path.GetFileName(RemotePath);
                new Thread(new ThreadStart(new BinaryClient(PeerIP.ToString(), Port, RemotePath, this, FileLength).ReceiveFile)).Start();
            }
            else
                throw new BullshitException("Remote peer refused to send file: " + RemotePath);
        }

        /// <summary>
        /// Get a Chat message, and send it to the interface
        /// </summary>
        /// <param name="Visibility">Visibility of the message (public/private)</param>
        /// <param name="FromUserName">The user that send this message</param>
        /// <param name="Message">The message itself</param>
        public void ReceiveChatMessage(string Visibility, string FromUserName, string Message)
        {
            string chatline = FromUserName + ": (" + Visibility + ") " + Message;
            Console.WriteLine("Chat: " + FromUserName + ": (" + Visibility + ") " + Message);
            ChatMessageDelegate cmd = new ChatMessageDelegate(_Interface.ChatMessage);
            _Interface.Invoke(cmd, new Object[] { chatline });
        }

        /// <summary>
        /// Gets the search object
        /// </summary>
        public Search.Search Search
        {
            get
            {
                return _Search;
            }
        }
        /// <summary>
        /// Gets the Protocol
        /// </summary>
        public Protocol.Protocol Protocol
        {
            get
            {
                return _Protocol;
            }
        }

        /// <summary>
        /// Return the peer list
        /// </summary>
        public PeerList Peers
        {
            get
            {
                return _Peers;
            }
        }

        /// <summary>
        /// Add peer to Peer list and send "Identify yourself" command to peer
        /// </summary>
        /// <param name="IPAddr">IP address of peer as string</param>
        public void AddPeer(string IPAddr)
        {
            try
            {
                int port = Configuration.ProtocolPort();
                IPAddress ip = IPAddress.Parse(IPAddr);
                ProtocolWriter pw = new ProtocolWriter(ip, port);
                ProtocolWriter.IdentifyObject id = pw.Identify();
                _Peers.NewPeer(ip);
                _Peers.SetPeerName(IPAddr, id.Name);
                _Interface.AddUser(id.Name);
                Console.WriteLine(id.IP + " " + id.Name);
            }
            catch(Exception e)
            {
                throw new BullshitException("Yuor IP adrizzle iz teh bullshit (" + e.Message + ")");
            }
        }

        /// <summary>
        /// Send chat message to everybody
        /// </summary>
        /// <param name="message">The message to send</param>
        public void SendChatMessage(string message)
        {
            _Interface.ChatMessage(
                Configuration.Username() + ": (public) " + message
            );
            foreach (Peer p in _Peers.Peers)
            {
                try
                {
                    ProtocolWriter pw = new ProtocolWriter(p.IPAddr, Configuration.ProtocolPort());
                    pw.Chat("public", Configuration.Username(), message);
                }
                catch (Exception e)
                {
                    throw new BullshitException
                        ("NO WAI!! Teh " + p.UserName + " iz not resp0ndz00ring (" + e.Message + ")");
                }
            }
        }

        /// <summary>
        /// IInterface object that will display status changes
        /// </summary>
        public GraphicalInterface Interface
        {
            set
            {
                _Interface = value;
            }
        }

        /// <summary>
        /// Download a peerlist from a website
        /// </summary>
        /// <param name="URL"></param>
        /// <returns></returns>
        public XmlDocument DownloadPeerList(string URL)
        {
            XmlDocument TmpDocument = new XmlDocument();
            TmpDocument.LoadXml(GetWebRequest(URL));
            return TmpDocument;
        }

        /// <summary>
        /// Load a peerlist from a local resource
        /// </summary>
        /// <param name="PeerDoc"></param>
        /// <returns></returns>
        public ArrayList LoadPeerList(XmlDocument PeerDoc)
        {
            int Count = 0;
            ArrayList AddedPeers = new ArrayList();
            foreach (XmlNode IPNode in PeerDoc.LastChild.ChildNodes)
            {
                try
                {
                    AddPeer(IPNode.InnerText);
                    AddedPeers.Add(IPNode.InnerText);
                    Count++;
                    
                }
                catch
                {

                }

            }

            return AddedPeers;
        }

        /// <summary>
        /// Get data from the net
        /// </summary>
        /// <param name="sURL">Url to the resource</param>
        /// <returns>string with the resource</returns>
        public string GetWebRequest(string sURL)
        {
            string TmpReturn = string.Empty;

            try
            {
                WebRequest wrGETURL;
                wrGETURL = WebRequest.Create(sURL);

                WebProxy myProxy = new WebProxy("myproxy", 80);
                myProxy.BypassProxyOnLocal = true;

                wrGETURL.Proxy = WebProxy.GetDefaultProxy();

                Stream objStream;
                objStream = wrGETURL.GetResponse().GetResponseStream();

                StreamReader objReader = new StreamReader(objStream);



                string sLine = "";
                int i = 0;

                while (sLine != null)
                {
                    i++;
                    sLine = objReader.ReadLine();
                    if (sLine != null)
                        TmpReturn = TmpReturn + sLine;
                }

                return TmpReturn;
            }
            catch
            {
                throw new BullshitException("Error while downloading file");
            }

        }


        /// <summary>
        /// Calculate a string from a bytesize to a human readable format
        /// </summary>
        /// <param name="ByteSize">Size in bytes</param>
        /// <returns>Size in human readable format</returns>
        public static string CalcFromByte(string ByteSize)
        {
            // int i = 0;
            // long Bytes = Convert.ToInt64(ByteSize);
            string[] sizes = { "B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
            /*
            while (Bytes > 1024)
            {
                Bytes /= 1024;
                i++;
            }
            */
            int i = 0;
            long b = Convert.ToInt64(ByteSize);
            for (; b > 1024; b /= 1024, i++)
            {}
            return Convert.ToString(b) + " " + sizes[i];
        }

        /// <summary>
        /// Return the lowest available chess listen port
        /// </summary>
        /// <returns>Lowest available chess port</returns>
        private int GetLowestChessListenPort()
        {
            return _ChessPort++;
        }

        /// <summary>
        /// Send an invitation for a game of chess to a peer
        /// </summary>
        /// <param name="UserIndex">Index of the user to be invited</param>
        public void InviteForChess(int UserIndex)
        {
            Console.WriteLine("Nexus.InviteForChess(" + UserIndex.ToString() + ")"); 

            // get remote IP address and a local port
            IPAddress PeerIP = _Peers.GetPeer(UserIndex).IPAddr;
            int LocalPort = GetLowestChessListenPort();

            // start chess server
            ChessProtocol cp = new ChessProtocol(LocalPort, this);

            // send invitation
            ProtocolWriter pw = new ProtocolWriter(PeerIP, Configuration.ProtocolPort());
            pw.InitGame(Configuration.Username(), LocalPort);
        }

        /// <summary>
        /// Handle invitation for chess game by telling GraphicalInterface to handle the
        /// invitation.
        /// </summary>
        /// <param name="username">Username of the user</param>
        /// <param name="port">Port</param>
        public void ChessGameRequest(string username, int port)
        {
            Console.WriteLine("Nexus.ChessGameRequest(" + username + "," + port.ToString() + ")"); 
            bool result = false;
            ChessGameRequestDelegate cgrd = new ChessGameRequestDelegate(_Interface.ChessGameRequest);
            _Interface.BeginInvoke(cgrd, new Object[] { result, username, port});
        }

        /// <summary>
        /// Accept the chess invitation
        /// </summary>
        /// <param name="UserName">Username</param>
        /// <param name="Port">Port</param>
        public void AcceptChessGame(string UserName, int Port)
        {
            Console.WriteLine("Nexus.AcceptChessGame(" + UserName + "," + Port.ToString() + ")");

            // get peer's IP + protocol port
            IPAddress IPAddr = _Peers.GetPeer(UserName).IPAddr;
            int RemotePort = Configuration.ProtocolPort();

            // send acceptance message
            ProtocolWriter pw = new ProtocolWriter(IPAddr, RemotePort);
            pw.AcceptGame(Configuration.Username(), Port);

            // connect to chess server
            ChessProtocol cp = new ChessProtocol(IPAddr, Port);
        }

        /// <summary>
        /// Reject the chess invitation
        /// </summary>
        /// <param name="UserName">Username</param>
        /// <param name="Port">Port</param>
        public void RejectChessGame(string UserName, int Port)
        {
            Console.WriteLine("Nexus.RejectChessGame(" + UserName + "," + Port.ToString() + ")");

            // get peer's IP + protocol port
            IPAddress IPAddr = _Peers.GetPeer(UserName).IPAddr;
            int RemotePort = Configuration.ProtocolPort();

            // send rejection message
            ProtocolWriter pw = new ProtocolWriter(IPAddr, RemotePort);
            pw.RejectGame(Configuration.Username(), Port);
        }

        /// <summary>
        /// Create a chess form in the main thread using a wretched delegate.
        /// </summary>
        /// <param name="cg">ChessGame</param>
        public void CreateChessFormForChessGame(ChessGame cg)
        {
            CreateChessFormDelegate CreateChessForm
                = new CreateChessFormDelegate(_Interface.CreateChessForm);
            _Interface.Invoke(CreateChessForm, new object[] { cg });
        }
    }
}
