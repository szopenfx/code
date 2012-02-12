using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using KISP.Nexus;
using KISP.Binary;

namespace KISP.Protocol
{
    /// <summary>
    /// Handles protocol request
    /// </summary>
    class ProtocolHandler
    {
        private Socket ProtocolSocket;
        private Nexus.Nexus NexusPointer;
        private IPAddress RemoteIP;


        /// <summary>
        /// Sets the ProtocolSocket
        /// </summary>
        /// <param name="PassedSocket">The socket to handle</param>
        /// <param name="IP">IP that is handled</param>
        /// <param name="PassedNexusPointer">Pointer to the Nexus</param>
        public ProtocolHandler(Socket PassedSocket, Nexus.Nexus PassedNexusPointer, IPAddress IP)
        {
            NexusPointer = PassedNexusPointer;
            ProtocolSocket = PassedSocket;
            RemoteIP = IP;
        }


        /// <summary>
        /// Handles the ProtocolSocket, invokes HandleProtocolCommand() and returns the output to the Socket
        /// </summary>
        public void HandleSocket()
        {

                NetworkStream ProtocolNetworkStream = new NetworkStream(ProtocolSocket);
                StreamWriter ProtocolNetworkStreamWriter = new StreamWriter(ProtocolNetworkStream);
                StreamReader ProtocolStreamReader = new StreamReader(ProtocolNetworkStream);

                string Line = ProtocolStreamReader.ReadLine();
                ProtocolNetworkStreamWriter.WriteLine(HandleProtocolCommand(Line));
                ProtocolNetworkStreamWriter.Flush();
            
            
        }

        /// <summary>
        /// Handles a protocol command, and invokes the appropriate functions in the Nexus
        /// </summary>
        /// <param name="ProtocolCommand">The protocolcommand</param>
        /// <returns>a string with a response to the protocolcommand</returns>
        private string HandleProtocolCommand(string ProtocolCommand)
        {
            Console.WriteLine("ProtocolHandler.HandleProtocolCommand(" + ProtocolCommand + ")");
            //Split the ProtocolCommand
            char[] Delimiter = new char[1];
            Delimiter[0] = ' ';
            string[] Params = ProtocolCommand.Split(Delimiter);

            //See what the command is
            switch (Params[0].ToString())
            {
                case "get":
                    {
                        if (NexusPointer.IsPortAvailable(Convert.ToInt32(Params[1])))
                        {
                            string FileName = string.Empty;
                            int i = 0;
                            for (i = 3; i < (Params.Length); i++)
                            {
                                FileName = FileName + " " + Params[i];
                            }
                            long FileLength = Convert.ToInt64(Params[2]);
                  
                            NexusPointer.SendFile(RemoteIP, FileName, Convert.ToInt32(Params[1]));
                            
                            return "ok get";
                        }
                        else
                        {
                            return "fail get Port " + Params[1] + " not available";

                        }
                    }
                case "search":
                    {
                        return NexusPointer.Search.Find(Params[1]).InnerXml;
                    }
                case "identify":
                    {
                        return Configuration.Username() + " " + Dns.Resolve(Environment.MachineName).AddressList[0].ToString();
                    }
                case "chat":
                    {
                        int i = 3;
                        string Message = string.Empty;
                        for (i = 3; i < (Params.Length); i++)
                        {
                            Message = Message + " " + Params[i];
                        }
                        NexusPointer.ReceiveChatMessage(Params[1], Params[2], Message);
                        break;
                    }
                case "initgame":
                    {
                        // vraag user of hij potje skaak wil doen
                        NexusPointer.ChessGameRequest(
                            Params[1],
                            Convert.ToInt32(Params[2])
                        );
                        return "ok";
                    }
                case "acceptgame":
                    {
                        return "ok";
                    }
                case "rejectgame":
                    {
                        return "ok";
                    }
                default:
                    {
                        return "fail syntax";
                    }
            }
            return "OK";
        }
    }
}
