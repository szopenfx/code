using System;
using System.Collections.Generic;
using System.Text;

namespace KISP.Chess
{
    /// <summary>
    /// Class that represents everything there is to know about a chess game
    /// </summary>
    public class ChessGame
    {
        private Board _Board = new Board();
        private ChessForm _ChessInterface;
        private ChessProtocol _ChessProtocol;
        private PlayerColor _MyColor;
        private PlayerColor _PlayerToMove = PlayerColor.White;
        private MoveChecker _MoveChecker;
        private PieceType _PromotionType = PieceType.Queen;

        /// <summary>
        /// Create new chess game object
        /// </summary>
        public ChessGame(ChessProtocol Protocol, PlayerColor MyColor)
        {
            _ChessProtocol = Protocol;
            _MyColor = MyColor;
            _MoveChecker = new MoveChecker(this);
        }

        /// <summary>
        /// Promotion type property
        /// </summary>
        public PieceType PromotionType
        {
            get
            {
                return _PromotionType;
            }
            set
            {
                _PromotionType = value;
            }
        }        

        /// <summary>
        /// Send chat message to opponent
        /// </summary>
        /// <param name="Line">Some witty comment</param>
        public void SendMessage(string Line)
        {
            _ChessProtocol.SendMessage(Line);
        }

        /// <summary>
        /// Yield the game to opponent
        /// </summary>
        public void Surrender()
        {
            _ChessProtocol.Surrender();
        }

        /// <summary>
        /// Abandon game
        /// </summary>
        public void EndGame()
        {
            _ChessProtocol.EndGame();
        }

        /// <summary>
        /// Offer draw to opponent
        /// </summary>
        public void OfferDraw()
        {
            _ChessProtocol.OfferDraw();
        }

        /// <summary>
        /// Set reference to externally created ChessForm
        /// </summary>
        /// <param name="TehChessForm">Reference to chess form object</param>
        public void SetChessInterface(ChessForm TehChessForm)
        {
            _ChessInterface = TehChessForm;
        }

        /// <summary>
        /// Create a ChessForm
        /// </summary>
        public void CreateChessInterface()
        {
            _ChessInterface = new ChessForm(this);
            _ChessInterface.Show();
        }

        /// <summary>
        /// Switch player-to-move color
        /// </summary>
        private void ChangeColor()
        {
            _PlayerToMove = 
                _PlayerToMove == PlayerColor.White
                ? PlayerColor.Black
                : PlayerColor.White;
        }

        /// <summary>
        /// Do a move that was initiated by the user
        /// </summary>
        /// <param name="FromX">X coordinate</param>
        /// <param name="FromY">Y coordinate</param>
        /// <param name="ToX">X coordinate</param>
        /// <param name="ToY">Y coordinate</param>
        public void UserMove(int FromX, int FromY, int ToX, int ToY)
        {
            Move m = new Move(FromX, FromY, ToX, ToY, "");
            if (_MoveChecker.IsLegalMove(m))
            {
                HistoryDelegate hd = new HistoryDelegate(_ChessInterface.AddToHistory);
                _Board.DoCastling(m);
                _Board.UpdateCastlingStatus(m);
                _Board.ApplyMove(m);
                _ChessProtocol.SendMove(m.ToString());
                string Line = "You: " + Coordinate.IndexToChess(FromX, FromY) + " > " + Coordinate.IndexToChess(ToX, ToY);

                ChangeColor();

                _ChessInterface.Invoke(hd, new object[] { Line });
                _ChessInterface.DrawPieces();
            }
            //else
            //    throw new ChessException("Illegal move!");
        }

        /// <summary>
        /// Execute a move that was initiated by a peer
        /// </summary>
        /// <param name="m">Remote opponent sends a move</param>
        public void PeerMove(Move m)
        {
            HistoryDelegate hd = new HistoryDelegate(_ChessInterface.AddToHistory);
            string Line = "Opponent: " + Coordinate.IndexToChess(m.From.X, m.From.Y) + " > " + Coordinate.IndexToChess(m.To.X, m.To.Y);
            _Board.DoCastling(m);
            _Board.UpdateCastlingStatus(m);
            _Board.ApplyMove(m);

            ChangeColor();

            _ChessInterface.Invoke(hd, new object[] { Line });
            _ChessInterface.DrawPieces();
        }

        /// <summary>
        /// Property: Current player to move
        /// </summary>
        public PlayerColor PlayerToMove
        {
            get
            {
                return _PlayerToMove;
            }
        }

        /// <summary>
        /// Property: Own color
        /// </summary>
        public PlayerColor MyColor
        {
            get
            {
                return _MyColor;
            }
        }

        /// <summary>
        /// Property: Other's color
        /// </summary>
        public PlayerColor OtherColor
        {
            get
            {
                return _MyColor == PlayerColor.White
                    ? PlayerColor.Black
                    : PlayerColor.White;
            }
        }

        /// <summary>
        /// Property: Board object
        /// </summary>
        public Board Board
        {
            get
            {
                return _Board;
            }
        }

        /// <summary>
        /// Dispose of the chess game and the chess form.
        /// </summary>
        public void Dispose()
        {
            DisposeDelegate dd = new DisposeDelegate(_ChessInterface.Kill);
            try
            {
                _ChessInterface.Invoke(dd, new object[0]);
            }
            catch
            {
                // TODO: find out exactly which exception is thrown here
                // Throws an exception. We can't seem to fix this, but it works, so we just
                // swallow the exception, and do nothing (Indian-C#-programming-style)
            }

            this.Dispose();
        }
    }
}
