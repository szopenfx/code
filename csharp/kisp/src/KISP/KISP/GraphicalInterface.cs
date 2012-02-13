using System;
using System.Collections.Generic;
using System.Collections;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.IO;
using System.Windows.Forms;
using System.Net;
using System.Diagnostics;
using System.Xml;
using System.Threading;

using KISP.Nexus;
using KISP.Chess;

namespace KISP.Interface.Graphical
{
    /// <summary>
    /// Delegate for the Chatsmessage
    /// </summary>
    /// <param name="username">Username for the chatmessage</param>
    public delegate void ChatMessageDelegate(string username);
    /// <summary>
    /// Delegate for the download progress
    /// </summary>
    /// <param name="FileName">Filename that has the progress</param>
    public delegate void DownloadProgressDelegate(string FileName);
    /// <summary>
    /// Delegate for increasing the upload
    /// </summary>
    public delegate void IncUploadDelegate();
    /// <summary>
    /// Delegate for decreasing the upload
    /// </summary>
    public delegate void DecUploadDelegate();
    /// <summary>
    /// Delegate for requesting a chess game
    /// </summary>
    /// <param name="IsOk">Is the request accepted</param>
    /// <param name="UserName">Username that sends the request</param>
    /// <param name="port">Port to start the chessgame on</param>
    public delegate void ChessGameRequestDelegate(out bool IsOk, string UserName, int port);
    
    /// <summary>
    /// Delegate for creating a chessgame form
    /// </summary>
    /// <param name="OwningChessGame">The chessgame</param>
    public delegate void CreateChessFormDelegate(ChessGame OwningChessGame);


    /// <summary>
    /// Graphical interface for KISP
    /// </summary>
    public partial class GraphicalInterface : Form 
    {
        private Nexus.Nexus _nexus;
        private int Uploads = 0;
        private int Downloads = 0;

        
        /// <summary>
        /// Sets the Nexus pointer
        /// </summary>
        /// <param name="nexus"></param>
        public GraphicalInterface(Nexus.Nexus nexus)
        {
            _nexus = nexus; 
            InitializeComponent();
            ListView_Search.View = View.Details;
            ListView_Search.FullRowSelect = true;

            
        }

        /// <summary>
        /// Increase the counter on the number of concurrent downloads
        /// </summary>
        public void IncDownloads()
        {
            Downloads++;
            Lbl_Downloads.Text = Convert.ToString(Downloads);
        }

        /// <summary>
        /// Decrease the counter on the number of concurrent downloads
        /// </summary>
        public void DecDownloads()
        {
            Downloads--;
            Lbl_Downloads.Tag = Convert.ToString(Downloads);
        }

        /// <summary>
        /// Increase the counter on the number of the concurrent uploads
        /// </summary>
        public void IncUploads()
        {
            Uploads++;
            Lbl_Uploads.Text = Convert.ToString(Uploads);
        }

        /// <summary>
        /// Decrease the counter on the number of the concurrent uploads
        /// </summary>
        public void DecUploads()
        {
            Uploads--;
            Lbl_Uploads.Text = Convert.ToString(Uploads);
        }

        /// <summary>
        /// OnLoad eventhandler
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void GraphicalInterface_Load(object sender, EventArgs e)
        {
            UpdateMediaButtons();
        }


        /// <summary>
        /// Update the userlist in the Configuration tab
        /// </summary>
        /// <param name="NewList">The new userlist</param>
        public void UpdateUserList(PeerList NewList)
        {
            ListBox_Names.Items.Clear();

            foreach (Peer CurrentPeer in NewList.Peers)
            {
                ListBox_Names.Items.Add(CurrentPeer.UserName);
            }
        }

        /// <summary>
        /// Eventhandler for the Add Peer button
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button3_Click(object sender, EventArgs e)
        {
            try
            {
                _nexus.AddPeer(TextBox_Add_Peer.Text);
                ListBox_Peers.Items.Add(TextBox_Add_Peer.Text);
                TextBox_Add_Peer.Clear();
            }
            catch (BullshitException ex)
            {
                MessageBox.Show(ex.Message, "NO WAI!!");
            }
        }

        /// <summary>
        /// Eventhandler for the Send Chat button
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button1_Click(object sender, EventArgs e)
        {
            _nexus.SendChatMessage(textBox1.Text);
            textBox1.Text = "";
        }

        #region IInterface Members

        /// <summary>
        /// Method to add a suer to the Names Listbox
        /// </summary>
        /// <param name="username">Username to be added</param>
        public void AddUser(string username)
        {
            ListBox_Names.Items.Add(username);
        }

        /// <summary>
        /// Displays a chatmessage to on the form
        /// </summary>
        /// <param name="message">The message to be displayed</param>
        public void ChatMessage(string message)
        {
            ListBox_Chat.Items.Add(message);
        }


       /// <summary>
       /// Display a search result on the Listview
       /// </summary>
       /// <param name="FileName">Filename</param>
       /// <param name="Size">Size</param>
       /// <param name="RemoteUserName">Username of the remote resource</param>
       /// <param name="RemoteIP">IP of the remote user</param>
       /// <param name="RemoteSource">Type of the remote source</param>
       /// <param name="FileType">Type of the file</param>
       public  void DisplaySearchResult(string FileName, long Size, string RemoteUserName, string RemoteIP, string RemoteSource, string FileType)
        {
            string[] Items = new string[7] { Path.GetFileName(FileName), Nexus.Nexus.CalcFromByte(Size.ToString()), RemoteUserName, RemoteIP, Path.GetExtension(FileName).Replace(".",""), RemoteSource, FileName };
            ListView_Search.Items.Add(new SearchItem(Items, Size));
        }

        /// <summary>
        /// Display a new file download in the Listview
        /// </summary>
        /// <param name="FileName">Filename</param>
        /// <param name="Size">Size</param>
        /// <param name="RemoteIP">IP of the remote user</param>
        public void DisplayFileDownload(string FileName, long Size, string RemoteIP)
        {
            string[] Items = new string[7] { Path.GetFileName(FileName), Nexus.Nexus.CalcFromByte(Size.ToString()), "0 KB", "0 %", "0 KB/s", "unknown", RemoteIP };
            ListView_Downloads.Items.Add(new DownLoadItem(Items, Size, this));
        }

        /// <summary>
        /// Increases the number of uploads
        /// </summary>
        /// <param name="FileName">FileName</param>
        /// <param name="RemoteIP">Remote IP</param>
        public void DisplayFileUpload(string FileName, string RemoteIP)
        {
            IncUploads();
        }

        /// <summary>
        /// Decreases the number of uploads
        /// </summary>
        public void DeleteFileUpload()
        {
            DecUploads();
        }


        #endregion
        /// <summary>
        /// Eventhandler for the Search button
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Btn_Search_Click(object sender, EventArgs e)
        {
            ListView_Search.Items.Clear();
            _nexus.PerformSearchOnPeers(TextBox_Search.Text);    
        }

        /// <summary>
        /// Eventhandler for a doubleclick in the Search listview
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ListView_Search_DblClick(object sender, EventArgs e)
        {
            Console.WriteLine("Double Clicked");
        }

        /// <summary>
        /// Eventhandler for the Get button
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Btn_Get_Click(object sender, EventArgs e)
        {
            foreach (SearchItem CurrentItem in ListView_Search.SelectedItems)
            {
                try
                {
                    if (GetIndexForDownloadListView(CurrentItem.SubItems[0].Text) == -1)
                    {
                        _nexus.GetFile(IPAddress.Parse(CurrentItem.SubItems[3].Text), CurrentItem.SubItems[6].Text, _nexus.GetNewDownloadPort(), CurrentItem.TotalByteCount);
                        DisplayFileDownload(CurrentItem.SubItems[0].Text, CurrentItem.TotalByteCount, CurrentItem.SubItems[3].Text);
                        IncDownloads();
                    }
                    else
                    {
                        MessageBox.Show("The file is already being downloaded", "Duplicate Download");
                    }
                }
                catch (Exception Ex)
                {
                    MessageBox.Show(Ex.Message, "Error while downloading file");
                }
 
            }

        }

        /// <summary>
        /// Checks if a download is completed
        /// </summary>
        /// <param name="FileName">Filename of the file</param>
        /// <returns>True if it is completed, false if not</returns>
        public bool DownloadCompleted(string FileName)
        {
            return ((DownLoadItem)ListView_Downloads.Items[GetIndexForDownloadListView(FileName)]).Completed;

        }

        /// <summary>
        /// Get the index on the listview for the given filename
        /// </summary>
        /// <param name="FileName">Filename</param>
        /// <returns>Index of the file in the listview, returns -1 if the filename was not present</returns>
        private int GetIndexForDownloadListView(string FileName)
        {
            int i = 1;
            for (i = 1; i <= ListView_Downloads.Items.Count; i++)
            {
                if (ListView_Downloads.Items[i -1].SubItems[0].Text == Path.GetFileName(FileName))
                {
                    return (i - 1);
                }
            }
            return -1;


        }

        /// <summary>
        /// Eventhandler for double click on the Search Listview
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ListView_Search_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            Btn_Get_Click(sender, e);
        }


        /// <summary>
        /// Add 1K to the download status of the given file
        /// </summary>
        /// <param name="FilePath">FilePath</param>
        public void Progress1KinFileDownload(string FilePath)
        {
            ((DownLoadItem)ListView_Downloads.Items[GetIndexForDownloadListView(FilePath)]).Increment1K();
        }

        /// <summary>
        /// Delete a peer in the Nexus Peerlist
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Btn_Delete_Peer_Click(object sender, EventArgs e)
        {
            _nexus.Peers.DeletePeer(((string)ListBox_Peers.Items[ListBox_Peers.SelectedIndex]));
            ListBox_Peers.Items.RemoveAt(ListBox_Peers.SelectedIndex);
            UpdateUserList(_nexus.Peers);    
        }

        /// <summary>
        /// Eventhandler for double click on the Downloads Listview
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ListView_Downloads_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            try
            {
                string FileName = ListView_Downloads.SelectedItems[0].SubItems[0].Text;
                FileName = Configuration.DownloadDir() + "\\" + FileName;
                ShowMedia(FileName);
            }
            catch
            {
                MessageBox.Show("Unable to playback file", "Playback error");
            }

        }

        /// <summary>
        /// Eventhandler for the resize of the MediaPlayer tabpage
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void tabPage5_SizeChanged(object sender, EventArgs e)
        {
            if (m_objVideoWindow != null)
            {
                m_objVideoWindow.SetWindowPosition(tabPage5.ClientRectangle.Left,
                    tabPage5.ClientRectangle.Top,
                    tabPage5.ClientRectangle.Width,
                    tabPage5.ClientRectangle.Height);
            }
        }


        /// <summary>
        /// Eventhandler for the About menu item click. Shows the aboutform
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void aboutToolStripMenuItem_Click(object sender, EventArgs e)
        {
            AboutForm AbForm = new AboutForm();
            AbForm.Show();

        }

        /// <summary>
        /// Eventhandler for the Exit menu item click. Exits KISP
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }


        /// <summary>
        /// Eventhandler for the Play file menu item click. Plays a given file
        /// in the internal mediaplayer
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void playFileToolStripMenuItem_Click(object sender, EventArgs e)
        {
            OpenFileDialog OFD = new OpenFileDialog();
            OFD.InitialDirectory = Configuration.DownloadDir();
            if (OFD.ShowDialog() == DialogResult.OK)
            {
                ShowMedia(OFD.FileName);
            }
        }

        /// <summary>
        /// Eventhandler for the Open mydownloads item click. Opens an explorer window
        /// with the path to the MyDownloads folder.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void openMyDownloadsToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Process Explorer = new Process();
            Explorer.StartInfo.Arguments = Configuration.DownloadDir();
            Explorer.StartInfo.FileName = "explorer.exe";
            Explorer.Start();          
        }

        /// <summary>
        /// Eventhandler for the Scan Peers button click. Scans the local net for peers
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Btn_Scan_Peers_Click(object sender, EventArgs e)
        {
            //Perform UDP Broadcast
        }

        /// <summary>
        /// Eventhandler for the Import Peerlist button. Imports a peerlist from
        /// a local XML file
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Btn_Import_Peerlist_Click(object sender, EventArgs e)
        {
            OpenFileDialog OFD = new OpenFileDialog();
            OFD.InitialDirectory = System.Environment.CurrentDirectory;
            if (OFD.ShowDialog() == DialogResult.OK)
            {
                XmlDocument PeerList = new XmlDocument();
                PeerList.Load(OFD.FileName);
                ArrayList AddedPeers = _nexus.LoadPeerList(PeerList);
                MessageBox.Show(AddedPeers.Count + " peers added");
                foreach (string AddedPeer in AddedPeers)
                {
                    ListBox_Peers.Items.Add(AddedPeer);
                }
            }
        }

        /// <summary>
        /// Eventhandler for the Download Peerlist button. Downloads a XML peerlist
        /// from the net
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Btn_Download_Peerlist_Click(object sender, EventArgs e)
        {
            try
            {
                ArrayList AddedPeers = _nexus.LoadPeerList(_nexus.DownloadPeerList("http://hal.demon.nl/~kisp/peerlist.xml"));
                MessageBox.Show(AddedPeers.Count + " peers added");
                foreach (string AddedPeer in AddedPeers)
                {
                    ListBox_Peers.Items.Add(AddedPeer);
                }
            }
            catch(Exception ex)
            {
                MessageBox.Show("Error while downloading peerlist: " + ex.Message , "Download Error");
            }

        }

        /// <summary>
        /// Mouseclick eventhandler for the mediaplayer (Right mouse button)
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void panel2_MouseClick(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Right)
            {
                MediaPlayer_ContextMenu.Show(panel2, e.Location);
            }
        }

        /// <summary>
        /// Eventhandler for the Play menu item, plays a media file
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void MenuItem_Play_Click(object sender, EventArgs e)
        {
            try
            {
                m_objMediaControl.Run();
                m_CurrentStatus = MediaStatus.Running;
                UpdateMediaButtons();
            }
            catch
            {

            }
        }

        /// <summary>
        /// Eventhandler for the Pause menu item. Pauses the mediaplayer
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void MenuItem_Pause_Click(object sender, EventArgs e)
        {
            m_objMediaControl.Pause();
            m_CurrentStatus = MediaStatus.Paused;
            UpdateMediaButtons();
        }

        /// <summary>
        /// Eventhandler for the Stop menu item. Stops the mediaplayer
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void MenuItem_Stop_Click(object sender, EventArgs e)
        {
            m_objMediaControl.Stop();
            StopMedia();
            m_CurrentStatus = MediaStatus.Stopped;
            UpdateMediaButtons();
        }

        /// <summary>
        /// Eventhandler for the Play Chess menu item in the chat window. Initiates
        /// a chess game start procedure with the peer.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void playchessToolStripMenuItem_Click(object sender, EventArgs e)
        {
            int UserIndex = ListBox_Names.SelectedIndex;
            _nexus.InviteForChess(UserIndex);
        }

        /// <summary>
        /// Invites this user to play chess with a peer
        /// </summary>
        /// <param name="IsOk">Is the challenged game accepted?</param>
        /// <param name="UserName">Username that requests the challenge</param>
        /// <param name="port">Port on which to connect</param>
        public void ChessGameRequest(out bool IsOk, string UserName, int port)
        {
            string msg = UserName + " invites you to a game of chess. Do you accept the challenge?";
            string un = Configuration.Username();
            DialogResult dr = MessageBox.Show(msg, "Chess game", MessageBoxButtons.YesNo);
            IsOk = dr == DialogResult.Yes;
            if (IsOk)
            {
                _nexus.AcceptChessGame(un, port);
            }
            else
            {
                _nexus.RejectChessGame(un, port);
            }
        }

        /// <summary>
        /// Creates a chessform
        /// </summary>
        /// <param name="TheChessGame">Pointer to the Chessgame</param>
        public void CreateChessForm(ChessGame TheChessGame)
        {
            ChessForm TheChessForm = new ChessForm(TheChessGame);
            TheChessForm.Show();
            TheChessGame.SetChessInterface(TheChessForm);
        }

        /// <summary>
        /// Eventhandler for the Send Mother To Goelag menu item. Sends peers mother
        /// to the Goelag
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void sendMotherToGoelagToolStripMenuItem_Click(object sender, EventArgs e)
        {
            _nexus.SendChatMessage("I will send your mother to the Goelag! You working-class peasant!");
        }

        /// <summary>
        /// Eventhandler for the HammerSickle menu item. ASCII arts a hammer/sickle and chats it
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void hammerSickleToolStripMenuItem_Click(object sender, EventArgs e)
        {
       _nexus.SendChatMessage(@"                     |\',"); 
       _nexus.SendChatMessage(@"        .' \\|       \'. ");
       _nexus.SendChatMessage(@"     __|   |~         \ :");
       _nexus.SendChatMessage(@"    /\ \_. \  /\      : |");
       _nexus.SendChatMessage(@"    \ \|  \ \/  \     / /");
       _nexus.SendChatMessage(@"     \/    \ \/\ '._.' /");
       _nexus.SendChatMessage(@"           /\ \ '.___.'");
       _nexus.SendChatMessage(@"          / /\ \");
       _nexus.SendChatMessage(@"         / /  \ \");
       _nexus.SendChatMessage(@"        / /    \ \");
       _nexus.SendChatMessage(@"        \/      \/");

        }

        /// <summary>
        /// Eventhandler for the Help menu item click. Views the help window
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void helpToolStripMenuItem1_Click(object sender, EventArgs e)
        {
            HelpForm NewHelpForm = new HelpForm();
            NewHelpForm.Show();
        }
    }


    /// <summary>
    /// Represents a download item, has convert methods for all different views
    /// </summary>
    class DownLoadItem : ListViewItem
    {
        long BytesCount = 0;
        long TotalByteCount = 0;
        DateTime BeginTime = DateTime.Now;
        public bool Completed = false;
        GraphicalInterface TheForm;

        /// <summary>
        /// Constructors, sets variables
        /// </summary>
        /// <param name="Items">The items in the listview</param>
        /// <param name="FileSize">Size of the file</param>
        /// <param name="_TheForm">Pointer to the form</param>
        public DownLoadItem(string[] Items, long FileSize, GraphicalInterface _TheForm)
            : base(Items)
        {
            TotalByteCount = FileSize;
            TheForm = _TheForm;
        }

        /// <summary>
        /// The upload increased with 1K
        /// </summary>
        public void Increment1K()
        {
            BytesCount = BytesCount + (1024 *100);

            //Downloaded
            string Size = Nexus.Nexus.CalcFromByte(BytesCount.ToString());
            this.SubItems[2].Text = Size;
            //Done
            if ((((BytesCount * 100) / TotalByteCount) == 99) || ((BytesCount * 100) / TotalByteCount) == 100)
            {
                this.SubItems[3].Text = "100 %";
                
                Completed = true;
            }
            else
            {
                if (((BytesCount * 100) / TotalByteCount) >= 100)
                {
                    this.SubItems[3].Text = "100 %";
                    TheForm.DecDownloads();
                    
                }
                else
                {
                    this.SubItems[3].Text = Convert.ToString(((BytesCount * 100) / TotalByteCount)) + " %";
                }
            }
            //Rate
            TimeSpan IncrementTime = DateTime.Now - BeginTime;
            int Rate = Convert.ToInt32(((BytesCount / IncrementTime.TotalSeconds) / 1024));
            this.SubItems[4].Text = Rate.ToString() + " KB/s";
            //ETA
            this.SubItems[5].Text = Convert.ToString(((TotalByteCount - BytesCount)  / (Rate * 1024)));
        }
    }

    /// <summary>
    /// Represents a search item
    /// </summary>
    class SearchItem : ListViewItem
    {
        public long TotalByteCount = 0;

        /// <summary>
        /// Sets the filesize in Bytes for this item. This is needed when downloading
        /// </summary>
        /// <param name="Items">The items of the listview</param>
        /// <param name="FileSize">Size of the file in bytes</param>
        public SearchItem(string[] Items, long FileSize)
            : base(Items)
        {
            TotalByteCount = FileSize;
        }

    }

}