using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.IO;
using System.Threading;
using System.Windows.Forms;

using KISP.Nexus;

namespace KISP.Chess
{
    /// <summary>
    /// Class that handles the chess protocol
    /// </summary>
    public class ChessProtocol
    {
        private ChessGame _ChessGame;

        private StreamWriter _ChessStreamWriter;
        private StreamReader _ChessStreamReader;
        private NetworkStream _ChessNetworkStream;
        
        private int _ListenPort = -1;
        private Nexus.Nexus _Nexus = null;

        /// <summary>
        /// Connect to remote chess server
        /// </summary>
        /// <param name="RemoteIP">IP address of remote peer</param>
        /// <param name="RemotePort">Port of remote peer</param>
        public ChessProtocol(IPAddress RemoteIP, int RemotePort)
        {
            IPEndPoint RemoteIPEndPoint = new IPEndPoint(RemoteIP, RemotePort);
            TcpClient ProtocolClient = new TcpClient();
            ProtocolClient.Connect(RemoteIPEndPoint);
            if (ProtocolClient.Connected)
            {
                _ChessNetworkStream = ProtocolClient.GetStream();
                _ChessStreamWriter = new StreamWriter(_ChessNetworkStream);
                _ChessStreamReader = new StreamReader(_ChessNetworkStream);
                _ChessGame = new ChessGame(this, PlayerColor.Black);
                _ChessGame.CreateChessInterface();
                new Thread(new ThreadStart(Read)).Start();
            }
            else
            {
                throw new Exception("Connection to client timed out");
            }
        }

        /// <summary>
        /// Start chess server
        /// </summary>
        /// <param name="Port">Port for server</param>
        /// <param name="Nexus">Reference to Nexus object</param>
        public ChessProtocol(int Port, Nexus.Nexus Nexus)
        {
            _ListenPort = Port;
            _Nexus = Nexus;
            new Thread(new ThreadStart(Listen)).Start();
        }

        /// <summary>
        /// Listen method to be executed by thread
        /// </summary>
        public void Listen()
        {
            TcpListener ChessListener = new TcpListener(_ListenPort);
            ChessListener.Start();
            Socket ChessSocket = ChessListener.AcceptSocket();
            if (ChessSocket.Connected)
            {
                _ChessNetworkStream = new NetworkStream(ChessSocket);
                _ChessStreamReader = new StreamReader(_ChessNetworkStream);
                _ChessStreamWriter = new StreamWriter(_ChessNetworkStream);
                _ChessGame = new ChessGame(this, PlayerColor.White);
                _Nexus.CreateChessFormForChessGame(_ChessGame);
                new Thread(new ThreadStart(Read)).Start();
            }
        }

        /// <summary>
        /// Read method to be executed by threads
        /// </summary>
        public void Read()
        {
            string Line = "";
            while (true)
            {
                // read line, write some debug output
                Line = _ChessStreamReader.ReadLine();
                Console.WriteLine("ChessProtocol.Read(" + Line + ")");

                // split line
                char[] Delimiter = { ' ' };
                string[] Params = Line.Split(Delimiter);

                // handle command
                switch (Params[0])
                {
                    case "move":
                        {
                            string Promotion = null;
                            if (Params.Length == 4)
                                Promotion = Params[3];
                            Move m = new Move(Params[1], Params[2], Promotion);
                            _ChessGame.PeerMove(m);
                            break;
                        }
                    case "stalemate":
                        {
                            MessageBox.Show("Stalemate!", "Stalemate! Did you feel lucky?");
                            _ChessGame.Dispose();
                            break;
                        }
                    case "check_mate":
                        {
                            MessageBox.Show("Checkmate!", "Checkmate! Did you feel lucky?");
                            _ChessGame.Dispose();
                            break;
                        }
                    case "message":
                        {
                            MessageBox.Show(Params[1] + ": " + Line.Substring(Line.IndexOf(Params[2])), "Message from user " + Params[1]);
                            break;
                        }
                    case "surrender":
                        {
                            MessageBox.Show("You have won!", "Remote user surrendered");
                            _ChessGame.Dispose();
                            break;
                        }
                    case "endgame":
                        {
                            MessageBox.Show("You have won!", "Remote user ended the game");
                            _ChessGame.Dispose();
                            break;
                        }
                    case "offer_draw":
                        {
                            DialogResult dr = MessageBox.Show("You are offered a draw, do you accept?", "Draw offer", MessageBoxButtons.YesNo);
                            if (dr == DialogResult.Yes)
                            {
                                DrawOk();
                                _ChessGame.Dispose();
                            }
                            else
                                DrawDeny();
                            break;
                        }
                    case "draw_ok":
                        {
                            MessageBox.Show("Draw accepted", "Your draw offer has been accepted");
                            _ChessGame.Dispose();
                            break;
                        }
                    case "draw_deny":
                        {
                            MessageBox.Show("Draw rejected", "Your draw offer has been rejected");
                            break;
                        }
                }
            }
        }

        /// <summary>
        /// Send command to peer
        /// </summary>
        /// <param name="Line">Any text</param>
        public void Write(string Line)
        {
            _ChessStreamWriter.WriteLine(Line);
            _ChessStreamWriter.Flush();
        }

        /// <summary>
        /// Send move to peer
        /// </summary>
        /// <param name="From">From coordinate</param>
        /// <param name="To">To coordinate</param>
        /// <param name="Promotion">Promotion</param>
        public void SendMove(string From, string To, string Promotion)
        {
            Write("move " + From + " " + To + " " + Promotion);
        }

        /// <summary>
        /// Send move to peer
        /// </summary>
        /// <param name="MoveString"></param>
        public void SendMove(string MoveString)
        {
            Write("move " + MoveString);
        }

        /// <summary>
        /// Send chat message to opponent
        /// </summary>
        /// <param name="Line">Some joke</param>
        public void SendMessage(string Line)
        {
            Write("message " + Configuration.Username() + " " + Line);
        }

        /// <summary>
        /// Send surrender command to opponnent
        /// </summary>
        public void Surrender()
        {
            Write("surrender");
        }

        /// <summary>
        /// User shuts down his game and gives win to opponent
        /// </summary>
        public void EndGame()
        {
            Write("endgame");
        }

        /// <summary>
        /// Send draw offer
        /// </summary>
        public void OfferDraw()
        {
            Write("offer_draw");
        }

        /// <summary>
        /// Send draw offer acceptance
        /// </summary>
        public void DrawOk()
        {
            Write("draw_ok");
        }

        /// <summary>
        /// Send draw offer rejection
        /// </summary>
        public void DrawDeny()
        {
            Write("draw_deny");
        }
    }
}

