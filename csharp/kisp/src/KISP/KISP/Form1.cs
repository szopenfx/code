using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Collections;
using KISP.Chess;
using System.Timers;

namespace KISP.Chess
{
    /// <summary>
    /// Delegate to add Chess history to the form
    /// </summary>
    /// <param name="Line">Line to be added to the form</param>
    public delegate void HistoryDelegate(string Line);

    /// <summary>
    /// Delegate to dispose the chessgame
    /// </summary>
    public delegate void DisposeDelegate();

    /// <summary>
    /// Form that represents the chessgame
    /// </summary>
    public partial class ChessForm : Form
    {
        private PictureBox[,] _BoardPictures = new PictureBox[8, 8];
        private PictureBox[,] _PiecePictures = new PictureBox[8, 8];

        private Hashtable _BlackPieceImageIndex = new Hashtable();
        private Hashtable _WhitePieceImageIndex = new Hashtable();

        private DateTime _StartTime;
        private DateTime _MoveTime;

        private ChessGame _Game;

        private Coordinate _LastClickedIndex = null;
        private Coordinate _LastClickedScreen = null;
        
        /// <summary>
        /// Constructor for chess form
        /// </summary>
        /// <param name="TheChessGame">Reference to chess game object</param>
        public ChessForm(ChessGame TheChessGame)
        {
            InitializeComponent();
            _Game = TheChessGame;
            Lbl_MyColor.Text = _Game.MyColor.ToString();
            if (_Game.MyColor == PlayerColor.Black)
            {
                Lbl_MyColor.ForeColor = Color.White;
                Lbl_MyColor.BackColor = Color.Black;
            }
            else
            {
                Lbl_MyColor.ForeColor = Color.Black;
                Lbl_MyColor.BackColor = Color.White;
            }
        }

        /// <summary>
        /// Switch label that indicates whose move it is
        /// </summary>
        public void ChangePlayerToMoveLabel()
        {
            if (_Game.PlayerToMove == PlayerColor.Black)
            {
                Lbl_PTM.ForeColor = Color.White;
                Lbl_PTM.BackColor = Color.Black;
                //Lbl_PTM.Text = "BLACK";
            }
            else
            {
                Lbl_PTM.ForeColor = Color.Black;
                Lbl_PTM.BackColor = Color.White;
                //Lbl_PTM.Text = "WHITE";
            }

        }

        /// <summary>
        /// Event: Form loaded
        /// </summary>
        /// <param name="sender">Sending object</param>
        /// <param name="e">Event information</param>
        private void ChessForm_Load(object sender, EventArgs e)
        {
            // Build the PieceImageIndex

            // Black
            _BlackPieceImageIndex.Add(PieceType.Pawn, 0);
            _BlackPieceImageIndex.Add(PieceType.Bishop, 1);
            _BlackPieceImageIndex.Add(PieceType.King, 2);
            _BlackPieceImageIndex.Add(PieceType.Knight, 3);
            _BlackPieceImageIndex.Add(PieceType.Queen, 4);
            _BlackPieceImageIndex.Add(PieceType.Rook, 5);

            // White
            _WhitePieceImageIndex.Add(PieceType.Pawn, 6);
            _WhitePieceImageIndex.Add(PieceType.Bishop, 7);
            _WhitePieceImageIndex.Add(PieceType.King, 8);
            _WhitePieceImageIndex.Add(PieceType.Knight, 9);
            _WhitePieceImageIndex.Add(PieceType.Queen, 10);
            _WhitePieceImageIndex.Add(PieceType.Rook, 11);

            // Draw the board
            BuildBoard();
            DrawPieces();

            _StartTime = DateTime.Now;
            MainTimer.Start();
        }

        /// <summary>
        /// Event: Timer tick elapsed
        /// </summary>
        /// <param name="sender">Sending object</param>
        /// <param name="e">Event information</param>
        private void MainTimer_Elapsed(object sender, EventArgs e)
        {
            Lbl_TotalTime.Text = GetTimeSpan(DateTime.Now, _StartTime);
            Lbl_MoveTime.Text = GetTimeSpan(DateTime.Now, _MoveTime);
        }

        /// <summary>
        /// Calculate time span between two DateTime objects
        /// </summary>
        /// <param name="Time1">Moment A</param>
        /// <param name="Time2">Moment B</param>
        /// <returns>Difference in hours as a string</returns>
        private string GetTimeSpan(DateTime Time1, DateTime Time2)
        {

            TimeSpan PlayTime = Time1 - Time2;
            char PadChar = '0';
            return PlayTime.Hours.ToString().PadLeft(2, PadChar) + ":" + PlayTime.Minutes.ToString().PadLeft(2, PadChar) + ":" + PlayTime.Seconds.ToString().PadLeft(2, PadChar);
        }

        /// <summary>
        /// Reset move time to now
        /// </summary>
        private void ResetMoveTime()
        {
            _MoveTime = DateTime.Now;
        }

        /// <summary>
        /// Put board images on screen
        /// </summary>
        void BuildBoard()
        {
            for (int Y = 0; Y < 8; Y++)
                for (int X = 0; X < 8; X++)
                {
                    // convert screen coordinate to index coordinate
                    Coordinate Coord =
                        _Game.MyColor == PlayerColor.White
                        ? Coordinate.FromWhiteScreen(X, Y)
                        : Coordinate.FromBlackScreen(X, Y);

                    // set properties of board picture
                    _BoardPictures[Y, X] = new PictureBox();
                    _BoardPictures[Y, X].Tag = Coord;
                    _BoardPictures[Y, X].Click += new EventHandler(ChessForm_Click);
                    _BoardPictures[Y, X].Location = new Point((X * 80) + 1, (Y * 80) + 1);
                    _BoardPictures[Y, X].Size = new Size(80, 80);
                    _BoardPictures[Y, X].SizeMode = PictureBoxSizeMode.CenterImage;
                    _BoardPictures[Y, X].BackColor = (X + Y) % 2 == 0
                        ? Color.Beige
                        : Color.Brown;
                    _BoardPictures[Y, X].Visible = true;

                    // add picture to chess panel
                    ChessPanel.Controls.Add(_BoardPictures[Y, X]);
                }
        }

        /// <summary>
        /// Set background color for a coordinate
        /// </summary>
        /// <param name="TheCoordinate">Coordinate of square</param>
        /// <param name="TheColor">Color of square</param>
        void SetBackGroundColor(Coordinate TheCoordinate, Color TheColor)
        {
            _BoardPictures[TheCoordinate.Y, TheCoordinate.X].BackColor = TheColor;
        }

        /// <summary>
        /// Set background color to original for a coordinate
        /// </summary>
        /// <param name="TheCoordinate">Coordinate to reset</param>
        void RestoreBackgroundColor(Coordinate TheCoordinate)
        {
            if ((TheCoordinate.X + TheCoordinate.Y) % 2 == 1)
                SetBackGroundColor(TheCoordinate, Color.Beige);
            else
                SetBackGroundColor(TheCoordinate, Color.White);
        }

        /// <summary>
        /// Event: User clicked on board
        /// </summary>
        /// <param name="Sender">Sending object</param>
        /// <param name="Event">Event information</param>
        void ChessForm_Click(object Sender, EventArgs Event)
        {
            // retrieve coordinate object from picturebox tag
            Coordinate Click = (Coordinate)((PictureBox)Sender).Tag;
            Console.WriteLine(Click);

            // if no square has been clicked yet
            if (_LastClickedIndex == null)
            {
                // and a piece is there, it's my color and it's my turn
                if (_Game.Board[Click] != null
                    && _Game.Board[Click].Color == _Game.MyColor
                    && _Game.PlayerToMove == _Game.MyColor)
                {
                    // save click coordinate
                    _LastClickedIndex = Click;

                    // save screen coordinate
                    _LastClickedScreen = new Coordinate(Click);
                    _LastClickedScreen.ToScreen(_Game.MyColor);

                    // set temporary background color on screen coordinate
                    SetBackGroundColor(_LastClickedScreen, Color.Gray);
                }
            }
            else // a square has been clicked before
            {
                // if it's a different coordinate than before
                if (_LastClickedIndex != Click)
                    // perform move
                    _Game.UserMove(_LastClickedIndex.X, _LastClickedIndex.Y, Click.X, Click.Y);

                // restore background color of last clicked square
                RestoreBackgroundColor(_LastClickedScreen);

                // now no square is selected
                _LastClickedIndex = null;
                _LastClickedScreen = null;
            }
        }

        /// <summary>
        /// Add move to history listbox
        /// </summary>
        /// <param name="Line">Representation of move</param>
        public void AddToHistory(string Line)
        {
            ListBox_History.Items.Add(Line);
        }

        /// <summary>
        /// Draw pieces on board
        /// </summary>
        public void DrawPieces()
        {
            int PieceIndex = 0;

            // for every row, for every column
            for (int Y = 0; Y < 8; Y++)
                for (int X = 0; X < 8; X++)
                {
                    // draw no piece by default
                    Image TheImage = null;
                    // if there is a piece
                    if (_Game.Board[Y, X] != null)
                    {
                        // find index of piece in Image_Pieces list
                        PieceIndex = _Game.Board[Y, X].Color == PlayerColor.White
                            ? (int) _WhitePieceImageIndex[_Game.Board[Y, X].Type]
                            : (int) _BlackPieceImageIndex[_Game.Board[Y, X].Type];
                        // acquire the reference to the image itself
                        TheImage = Image_Pieces.Images[PieceIndex];
                    }
                    // draw the piece at the right screen coordinate
                    if (_Game.MyColor == PlayerColor.White)
                        _BoardPictures[7 - Y, X].Image = TheImage;
                    else
                        _BoardPictures[Y, 7 - X].Image = TheImage;
                }
            ChangePlayerToMoveLabel();
            ResetMoveTime();
        }

        /// <summary>
        /// Event: User clicked send message button
        /// </summary>
        /// <param name="sender">Sending object</param>
        /// <param name="e">Event information</param>
        private void Btn_SendMessage_Click(object sender, EventArgs e)
        {
            StringInputDialog sid = new StringInputDialog();

            sid.ShowDialog();

            if (sid.OK == true)
            {

                _Game.SendMessage(sid.Message);
            }
        }

        /// <summary>
        /// Event: User clicked surrender button
        /// </summary>
        /// <param name="sender">Sending object</param>
        /// <param name="e">Event information</param>
        private void Btn_Surrender_Click(object sender, EventArgs e)
        {
            if (_Game.MyColor == _Game.PlayerToMove)
            {
                DialogResult dr = MessageBox.Show("Are you sure you wish to surrender", "Are you sure?", MessageBoxButtons.YesNo);

                if (dr == DialogResult.Yes)
                {
                    _Game.Surrender();
                    this.Close();
                }
            }
            else
            {
                MessageBox.Show("Please wait for your turn to surrender", "Surrender not possible");
            }
            
            
        }

        /// <summary>
        /// DESTRUYE! MATANDO GÜEROS!!!!
        /// </summary>
        public void Kill() // die! die! die!
        {
            this.Close(); // die! die! die!
        }
        // The state has scheduled you for disposal.

        /// <summary>
        /// Event: User clicked quit button
        /// </summary>
        /// <param name="sender">Sending object</param>
        /// <param name="e">Event information</param>        
        private void Btn_Quit_Click(object sender, EventArgs e)
        {
            DialogResult dr = MessageBox.Show("Are you sure you want to quit?", "Are you sure?", MessageBoxButtons.YesNo);

            if (dr == DialogResult.Yes)
            {
                _Game.EndGame();
                this.Close();
            }

        }

        /// <summary>
        /// Event: User clicked offer draw button
        /// </summary>
        /// <param name="sender">Sending object</param>
        /// <param name="e">Event information</param>
        private void Btn_OfferDraw_Click(object sender, EventArgs e)
        {
            if (_Game.MyColor == _Game.PlayerToMove)
            {
                DialogResult dr = MessageBox.Show("Are you sure you want to offer a draw?", "Are you sure?" , MessageBoxButtons.YesNo);

                if (dr == DialogResult.Yes)
                {
                    _Game.OfferDraw();

                }
            }
            else
            {
                MessageBox.Show("Please wait for your turn to offer a draw", "Draw offer not possible" );
            }

        }

        /// <summary>
        /// Retrieve current promotion piece type
        /// </summary>
        /// <returns>PieceType for a valid promotion piece</returns>
        public PieceType GetPromotionPieceType()
        {
            switch (Combo_Promotion.SelectedText)
            {
                case "Queen": return PieceType.Queen;
                case "Bishop": return PieceType.Bishop;
                case "Rook": return PieceType.Rook;
                case "Knight": return PieceType.Knight;
                default: return PieceType.Queen;
            }
        }
    }
}