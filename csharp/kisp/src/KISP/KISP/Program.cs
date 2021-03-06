using System;
using System.Collections.Generic;
using System.Text;
using System.Net;

using KISP.Protocol;
using KISP.Nexus;
using KISP.Search;
using KISP.Interface.Graphical;
using System.Windows.Forms;

namespace KISP
{
    /// <summary>
    /// Hyper main class that creates the main class (Nexus)
    /// </summary>
    class Program
    {
        private static Nexus.Nexus _nexus;

        /// <summary>
        /// Ye'olde maine method, ARRRRR!
        /// </summary>
        /// <param name="args">Arguments</param>
        [STAThread]
        static void Main(string[] args)
        {
            Console.Title = "Kim Il-Sung's Политбюро"; // =politburo
            
            Console.BackgroundColor = ConsoleColor.DarkRed;
            Console.ForegroundColor = ConsoleColor.Yellow;

            Console.Clear();

            // employ some white propaganda
            Console.WriteLine("");
            Console.WriteLine("                 !#########       #");
            Console.WriteLine("               !########!          ##!");
            Console.WriteLine("            !########!               ###");
            Console.WriteLine("         !##########                  ####");
            Console.WriteLine("       ######### #####                ######");
            Console.WriteLine("        !###!      !####!              ######");
            Console.WriteLine("          !           #####            ######!");
            Console.WriteLine("                        !####!         #######");
            Console.WriteLine("                           #####       #######");
            Console.WriteLine("                             !####!   #######!");
            Console.WriteLine("                                ####!########");
            Console.WriteLine("             ##                   ##########");
            Console.WriteLine("           ,######!          !#############");
            Console.WriteLine("         ,#### ########################!####!");
            Console.WriteLine("       ,####'     ##################!'    #####");
            Console.WriteLine("     ,####'            #######              !####!");
            Console.WriteLine("    ####'                                      #####");
            Console.WriteLine("    ~##                                          ##~");
            Console.WriteLine("             ALL YU0R WAREZ ARE BEL0NG TO CCCP");
            Console.WriteLine("");
            Console.WriteLine("          in soviet russia, pirate software find YOU!");
            Console.WriteLine("");
            
            // debug switch that allows for testing little parts of the program - mostly irrelevant
            int choice = 5;
            switch (choice * choice / choice) // optimize THAT, compiler! kekeke
            {
                case 1:
                    TestMainProtocol();
                    break;
                case 2:
                    TestConfiguration();
                    break;
                case 3:
                    TestSearch();
                    break;
                case 4:
                    TestMulticastSend();
                    break;
                case 5:
                    StartWindowsProgram();
                    break;
            }

            Console.ReadLine();
        }

        /// <summary>
        /// Test main protocol
        /// </summary>
        static void TestMainProtocol()
        {
            //Protocol.Protocol MainProtocol = new Protocol.Protocol(666);
            Nexus.Nexus TestNexus = new Nexus.Nexus();
            //TestNexus.DownloadFile(IPAddress.Parse("127.0.0.1"), @"c:\finish.log", 800);
        }

        /// <summary>
        /// Test configuration object
        /// </summary>
        static void TestConfiguration()
        {
            Console.WriteLine("user name: " + Configuration.Username());
            Console.WriteLine("shares: " + Configuration.ShareCount());
            for(int i = 0; i < Configuration.ShareCount(); i++)
            {
                Console.WriteLine("--- share type: " + Configuration.ShareType(i));
                switch(Configuration.ShareType(i))
                {
                    case "dir":
                        Console.WriteLine("dir: " + Configuration.LocalShareDir(i));
                        break;
                    case "FTP":
                        Console.WriteLine("user: " + Configuration.FTPShareUsername(i));
                        Console.WriteLine("pass: " + Configuration.FTPSharePassword(i));
                        Console.WriteLine("addr: " + Configuration.FTPShareAddress(i));
                        break;
                }
            } 
        }

        /// <summary>
        /// Do a test search
        /// </summary>
        static void TestSearch()
        {
            Search.Search MainSearch = new Search.Search();
            Console.WriteLine(MainSearch.Find("Dissection").InnerXml);
        }

        /// <summary>
        /// Try out multicast code
        /// </summary>
        static void TestMulticastSend()
        {
            String s = Console.ReadLine();
            switch (s)
            {
                case "send":
                    Console.WriteLine("Sending");
                    KISP.Multicast.Multicast mw = new KISP.Multicast.Multicast(null, false, true);
                    break;
                case "recv":
                    Console.WriteLine("Receiving");
                    KISP.Multicast.Multicast mr = new KISP.Multicast.Multicast(null, true, false);
                    break;
            }
        }

        /// <summary>
        /// Start windows form
        /// </summary>
        [STAThread]
        static void StartWindowsProgram()
        {
            try
            {
                _nexus = new Nexus.Nexus();
                Application.EnableVisualStyles();
                Application.SetCompatibleTextRenderingDefault(false);
                GraphicalInterface gi = new GraphicalInterface(_nexus);
                _nexus.Interface = gi;
                Application.Run(gi);
            }

            // thrown by kisp itself
            catch (BullshitException e)
            {
                MessageBox.Show(e.Message, "NO WAI!!");
            }

            // thrown by invocation targets
            catch (System.Reflection.TargetInvocationException e)
            {
                string Message = e.Message
                    + "\n"
                    + e.InnerException.Message
                    + "\n"
                    + e.StackTrace;

                string Caption = "TargetInvocationException: "
                    + e.InnerException.GetType().ToString();

                MessageBox.Show(Message, Caption);
            }

            // uncaught exceptions
            catch (Exception e)
            {
                string Message = e.Message
                    + "\n"
                    + e.StackTrace;

                string Caption = e.GetType().ToString();

                MessageBox.Show(Message, Caption);
            }
        }
    }
}
