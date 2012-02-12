using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;

namespace KISP.Chess
{
    /// <summary>
    /// Exception class for problems related to chess
    /// </summary>
    public class ChessException : Exception
    {
        /// <summary>
        /// The nice constructor that only calls the base constructor
        /// </summary>
        /// <param name="message"></param>
        public ChessException(string message) : base(message) 
        {
        }
    }

    /// <summary>
    /// Enumeration for defining a piece; NONE is used when a move does not end in a promotion
    /// </summary>
    public enum PieceType
    {
        /// <summary>
        /// This is a pawn
        /// </summary>
        Pawn = 1,
        /// <summary>
        /// This is a rook
        /// </summary>
        Rook = 2,
        /// <summary>
        /// We are the knights who say, NIE!
        /// </summary>
        Knight = 3,
        /// <summary>
        /// This is a bishop
        /// </summary>
        Bishop = 4,
        /// <summary>
        /// The queen 
        /// </summary>
        Queen = 5,
        /// <summary>
        /// The king
        /// </summary>
        King = 6,
        /// <summary>
        /// No piece
        /// </summary>
        NONE = 7
    }

    /// <summary>
    /// Enumeration for defining the color of a player
    /// </summary>
    public enum PlayerColor
    {
        /// <summary>
        /// The color of one player
        /// </summary>
        White = 1,
        /// <summary>
        /// The color of another player
        /// </summary>
        Black = 2
    }

    /// <summary>
    /// Class that represents a piece
    /// </summary>
    public class Piece
    {
        /// <summary>
        /// Type of piece
        /// </summary>
        public PieceType _Type;

        /// <summary>
        /// Color of piece
        /// </summary>
        public PlayerColor _Color;

        /// <summary>
        /// Create a piece from a Piece Type and a Player Color
        /// </summary>
        /// <param name="NewType">Type of piece</param>
        /// <param name="NewColor">Color of piece</param>
        public Piece(PieceType NewType, PlayerColor NewColor)
        {
            _Type = NewType;
            _Color = NewColor;
        }

        /// <summary>
        /// String representation of piece
        /// </summary>
        /// <returns>String that represents the piece</returns>
        public override string ToString()
        {
            return _Color + " " + _Type;
        }

        /// <summary>
        /// PieceType property
        /// </summary>
        public PieceType Type
        {
            get
            {
                return _Type;
            }
            set
            {
                _Type = value;
            }
        }

        /// <summary>
        /// PlayerColor property
        /// </summary>
        public PlayerColor Color
        {
            get
            {
                return _Color;
            }
        }
    }

    /// <summary>
    /// This class represents a coordinate on the chess board.
    /// Coordinate can be set by chess coordinates (e.g., A2) or by index coordinate (e.g.,
    /// x=1, y=0). Coordinates can also be converted from and to screen coordinates for either
    /// black or white; chess/A2 = index/x:0,y:2 = black screen/x:7,y:2 = white screen/x:0,y:6.
    /// </summary>
    public class Coordinate
    {
        private int _X;
        private int _Y;

        /// <summary>
        /// Create Coordinate from Chess notation
        /// </summary>
        /// <param name="ChessCoord">String representation of chess coordinate</param>
        public Coordinate(string ChessCoord)
        {
            ChessToIndex(ChessCoord, out _X, out _Y);
        }

        /// <summary>
        /// Create coordinate from Indices
        /// </summary>
        /// <param name="IndexX">X index</param>
        /// <param name="IndexY">Y index</param>
        public Coordinate(int IndexX, int IndexY)
        {
            _X = IndexX;
            _Y = IndexY;
        }

        /// <summary>
        /// Create coordinate from another coordinate
        /// </summary>
        /// <param name="Coord">Other coordinate to take values from</param>
        public Coordinate(Coordinate Coord)
        {
            _X = Coord.X;
            _Y = Coord.Y;
        }

        /// <summary>
        /// X coordinate property
        /// </summary>
        public int X
        {
            get
            {
                return _X;
            }
        }

        /// <summary>
        /// Y coordinate property
        /// </summary>
        public int Y
        {
            get
            {
                return _Y;
            }
        }

        /// <summary>
        /// Return string representation of coordinate
        /// </summary>
        /// <returns>String representation of coordinate</returns>
        public override string ToString()
        {
            return IndexToChess(_X, _Y);
        }

        /// <summary>
        /// Convert index coordinate to chess coordinate
        /// </summary>
        /// <returns>String representation of coordinate</returns>
        public string ToChessCoord()
        {
            return Coordinate.IndexToChess(_X, _Y);
        }


        /// <summary>
        /// Compare the coordinates
        /// </summary>
        /// <param name="obj">Object to compare to</param>
        /// <returns>True of coordinates are equal</returns>
        public override bool Equals(object obj)
        {
            Coordinate Other = (Coordinate)obj;
            return (_X == Other.X) && (_Y == Other.Y);
        }

        /// <summary>
        /// Convert a generic screen coordinate to index coordinate
        /// </summary>
        /// <param name="playercolor">Color of player to convert for</param>
        public void FromScreen(PlayerColor playercolor)
        {
            if (playercolor == PlayerColor.White)
                FromWhiteScreen();
            else
                FromBlackScreen();
        }

        /// <summary>
        /// Convert index coordinate to a screen coordinate
        /// </summary>
        /// <param name="playercolor">Color of player to convert for</param>
        public void ToScreen(PlayerColor playercolor)
        {
            if (playercolor == PlayerColor.White)
                ToWhiteScreen();
            else
                ToBlackScreen();
        }

        /// <summary>
        /// Convert black screen coordinate to index coordinate
        /// </summary>
        /// <param name="x">Index X</param>
        /// <param name="y">Index Y</param>
        /// <returns>Coordinate with appropriate values</returns>
        public static Coordinate FromBlackScreen(int x, int y)
        {
            Coordinate.BlackScreenToIndex(ref x, ref y);
            return new Coordinate(x, y);
        }

        /// <summary>
        /// Convert black screen coordinate to index coordinate
        /// </summary>
        public void FromBlackScreen()
        {
            Coordinate.BlackScreenToIndex(ref _X, ref _Y);
        }

        /// <summary>
        /// Convert index coordinate to black screen coordinate
        /// </summary>
        public void ToBlackScreen()
        {
            Coordinate.IndexToBlackScreen(ref _X, ref _Y);
        }

        /// <summary>
        /// Convert white screen coordinate to index coordinate
        /// </summary>
        /// <param name="x">Index X</param>
        /// <param name="y">Index Y</param>
        /// <returns></returns>
        public static Coordinate FromWhiteScreen(int x, int y)
        {
            Coordinate.WhiteScreenToIndex(ref x, ref y);
            return new Coordinate(x, y);
        }

        /// <summary>
        /// Convert white screen coordinate to index coordinate
        /// </summary>
        public void FromWhiteScreen()
        {
            Coordinate.WhiteScreenToIndex(ref _X, ref _Y);
        }

        /// <summary>
        /// Convert index coordinate to white screen coordinate
        /// </summary>
        public void ToWhiteScreen()
        {
            Coordinate.IndexToWhiteScreen(ref _X, ref _Y);
        }

        /// <summary>
        /// Calculate the distances the piece traveled
        /// </summary>
        /// <param name="Other">Another coordinate</param>
        /// <param name="XDelta">Horizontal distance</param>
        /// <param name="YDelta">Vertical distance</param>
        public void Delta(Coordinate Other, out int XDelta, out int YDelta)
        {
            XDelta = _X - Other.X;
            YDelta = _Y - Other.Y;
        }

        /// <summary>
        /// Calculate the absolute distances the piece traveled
        /// </summary>
        /// <param name="Other">Another coordinate</param>
        /// <param name="XDelta">Horizontal distance</param>
        /// <param name="YDelta">Vertical distance</param>
        public void AbsDelta(Coordinate Other, out int XDelta, out int YDelta)
        {
            Delta(Other, out XDelta, out YDelta);
            XDelta = Math.Abs(XDelta);
            YDelta = Math.Abs(YDelta);
        }

        /// <summary>
        /// Return a kind of a direction vector of -1, 0 or 1 for x and y
        /// </summary>
        /// <param name="Other">Another coordinate</param>
        /// <param name="XDirection">Horizontal direction</param>
        /// <param name="YDirection">Vertical direction</param>
        public void Direction(Coordinate Other, out int XDirection, out int YDirection)
        {
            int XDelta;
            int YDelta;
            Delta(Other, out XDelta, out YDelta);

            XDirection = XDelta < 0 ? -1
                : XDelta == 0 ? 0
                : 1;

            YDirection = YDelta < 0 ? -1
                : YDelta == 0 ? 0
                : 1;
        }

        /// <summary>
        /// Convert from Chess to Index
        /// </summary>
        /// <param name="ChessCoord">Chess coordinate as string</param>
        /// <param name="X">X Coordinate</param>
        /// <param name="Y">Y Coordinate</param>
        public static void ChessToIndex(string ChessCoord, out int X, out int Y)
        {
            const string Cols = "ABCDEFGH";
            const string Rows = "12345678";
            X = Cols.IndexOf(ChessCoord[0]);
            Y = Rows.IndexOf(ChessCoord[1]);
        }

        /// <summary>
        /// Convert from Index to Chess
        /// </summary>
        /// <param name="X">X index coordinate</param>
        /// <param name="Y">Y index coordinate</param>
        /// <returns>Chess coordinate</returns>
        public static string IndexToChess(int X, int Y)
        {
            return "ABCDEFGH"[X].ToString() 
                 + "12345678"[Y].ToString();
        }

        /// <summary>
        /// Convert from Black Screen to Index
        /// </summary>
        /// <param name="X">X coordinate (reference)</param>
        /// <param name="Y">Y coordinate (reference)</param>
        public static void BlackScreenToIndex(ref int X, ref int Y)
        {
            X = 7 - X;
        }
        
        /// <summary>
        /// Convert from Index to Black Screen
        /// </summary>
        /// <param name="X">X coordinate (reference)</param>
        /// <param name="Y">Y coordinate (reference)</param>
        public static void IndexToBlackScreen(ref int X, ref int Y)
        {
            X = 7-X;
        }

        /// <summary>
        /// Convert from White Screen to Index
        /// </summary>
        /// <param name="X">X coordinate (reference)</param>
        /// <param name="Y">Y coordinate (reference)</param>
        public static void WhiteScreenToIndex(ref int X, ref int Y)
        {
            Y = 7-Y;
        }

        /// <summary>
        /// Convert from Index to White Screen
        /// </summary>
        /// <param name="X">X coordinate (reference)</param>
        /// <param name="Y">Y coordinate (reference)</param>
        public static void IndexToWhiteScreen(ref int X, ref int Y)
        {
            Y = 7-Y;
        }
    }

    /// <summary>
    /// This class represents a move as a set of two coordinates, a promotion piece type 
    /// the optional capture of a piece.
    /// </summary>
    public class Move
    {
        private Coordinate _From;
        private Coordinate _To;
        private PieceType _Promotion;
        private Piece _Capture;

        /// <summary>
        /// Create Move from two Chess Coordinates and a Promotion string
        /// </summary>
        /// <param name="From">First chess coordinate</param>
        /// <param name="To">Second chess coordinate</param>
        /// <param name="Promotion">Type to promote to</param>
        public Move(string From, string To, string Promotion)
        {
            _From = new Coordinate(From);
            _To = new Coordinate(To);
            _Promotion = PieceStrToType(Promotion);
        }

        /// <summary>
        /// Create Move from two Index Coordinates and a Promotion string
        /// </summary>
        /// <param name="FromX">Source X coordinate</param>
        /// <param name="FromY">SourceY coordinate</param>
        /// <param name="ToX">Destination X coordinate</param>
        /// <param name="ToY">Destination Y coordinate</param>
        /// <param name="Promotion">Type to promote to</param>
        public Move(int FromX, int FromY, int ToX, int ToY, string Promotion)
        {
            _From = new Coordinate(FromX, FromY);
            _To = new Coordinate(ToX, ToY);
            _Promotion = PieceStrToType(Promotion);
        }

        /// <summary>
        /// Return string representation of move
        /// </summary>
        /// <returns>String representation of move</returns>
        public override string ToString()
        {
            return From.ToString() + " " + To.ToString() + " " + _Promotion;
        }

        /// <summary>
        /// From coordinate property
        /// </summary>
        public Coordinate From
        {
            get
            {
                return _From;
            }
        }

        /// <summary>
        /// To coordinate property
        /// </summary>
        public Coordinate To
        {
            get
            {
                return _To;
            }
        }

        /// <summary>
        /// The piece captured by this move [gets set on Board.ApplyMove]
        /// </summary>
        public Piece Capture
        {
            get
            {
                return _Capture;
            }
            set
            {
                _Capture = value;
            }                
        }

        /// <summary>
        /// Convert a Promotion string to a PieceType. This method reeks of bad OO.
        /// </summary>
        /// <param name="PieceID">ID string that represents a piece</param>
        /// <returns>PieceType identifier</returns>
        private static PieceType PieceStrToType(string PieceID)
        {
            switch (PieceID)
            {
                case null:
                case "":
                case "NONE":
                    {
                        return PieceType.NONE; 
                    }
                case "R":
                    { 
                        return PieceType.Rook; 
                    }
                case "N": 
                    { 
                        return PieceType.Knight; 
                    }
                case "B": 
                    { 
                        return PieceType.Bishop; 
                    }
                case "Q": 
                    { 
                        return PieceType.Queen; 
                    }
                case "K": 
                    { 
                        return PieceType.King; 
                    }
                case "p": 
                    {
                        return PieceType.Pawn; 
                    }
                default: 
                    { 
                        throw new ChessException("Unrecognized piece ID: " + PieceID); 
                    }
            }
        }

        /// <summary>
        /// Calculate the distances the piece traveled
        /// </summary>
        /// <param name="XDelta">Return the horizontal difference of the move</param>
        /// <param name="YDelta">Return the vertical difference of the move</param>
        public void Delta(out int XDelta, out int YDelta)
        {
            _To.Delta(_From, out XDelta, out YDelta);
        }

        /// <summary>
        /// Calculate the absolute distances the piece traveled
        /// </summary>
        /// <param name="XDelta">See Delta</param>
        /// <param name="YDelta">See Delta</param>
        public void AbsDelta(out int XDelta, out int YDelta)
        {
            _To.AbsDelta(_From, out XDelta, out YDelta);
        }

        /// <summary>
        /// Return a kind of a direction vector of -1, 0 or 1 for x and y
        /// </summary>
        /// <param name="XDirection">An int indicating the horizontal direction of the move</param>
        /// <param name="YDirection">An int indicating the vertical direction of the move</param>
        public void Direction(out int XDirection, out int YDirection)
        {
            _To.Direction(_From, out XDirection, out YDirection);
        }
    }

    /// <summary>
    /// Representation of a chess board
    /// </summary>
    public class Board
    {
        private Piece[,] _Board = new Piece[8, 8];
        
        // status of white castling ability
        private bool _WhiteCanCastleShort = true;
        private bool _WhiteCanCastleLong = true;

        // status of black castling ability
        private bool _BlackCanCastleShort = true;
        private bool _BlackCanCastleLong = true;

        // locations of pieces relevant to white castling
        private Coordinate _WhiteKing = new Coordinate(0, 4);
        private Coordinate _WhiteRookShort = new Coordinate(0, 0);
        private Coordinate _WhiteRookLong = new Coordinate(0, 7);

        // locations of pieces relevant to black castling
        private Coordinate _BlackKing = new Coordinate(7, 4);
        private Coordinate _BlackRookShort = new Coordinate(7, 0);
        private Coordinate _BlackRookLong = new Coordinate(7, 7);

        /// <summary>
        /// Initialize by populating the board
        /// </summary>
        public Board()
        {
            PopulateBoard();
        }

        /// <summary>
        /// Create new Array[8,8] for a Board and put pieces on it
        /// </summary>
        private void PopulateBoard()
        {
            _Board = new Piece[8, 8];

            _Board[0, 0] = new Piece(PieceType.Rook, PlayerColor.White);
            _Board[0, 1] = new Piece(PieceType.Knight, PlayerColor.White);
            _Board[0, 2] = new Piece(PieceType.Bishop, PlayerColor.White);
            _Board[0, 3] = new Piece(PieceType.Queen, PlayerColor.White);
            _Board[0, 4] = new Piece(PieceType.King, PlayerColor.White);
            _Board[0, 5] = new Piece(PieceType.Bishop, PlayerColor.White);
            _Board[0, 6] = new Piece(PieceType.Knight, PlayerColor.White);
            _Board[0, 7] = new Piece(PieceType.Rook, PlayerColor.White);

            _Board[7, 0] = new Piece(PieceType.Rook, PlayerColor.Black);
            _Board[7, 1] = new Piece(PieceType.Knight, PlayerColor.Black);
            _Board[7, 2] = new Piece(PieceType.Bishop, PlayerColor.Black);
            _Board[7, 3] = new Piece(PieceType.Queen, PlayerColor.Black);
            _Board[7, 4] = new Piece(PieceType.King, PlayerColor.Black);
            _Board[7, 5] = new Piece(PieceType.Bishop, PlayerColor.Black);
            _Board[7, 6] = new Piece(PieceType.Knight, PlayerColor.Black);
            _Board[7, 7] = new Piece(PieceType.Rook, PlayerColor.Black);

            for (int i = 0; i < 8; i++)
            {
                _Board[1, i] = new Piece(PieceType.Pawn, PlayerColor.White);
                _Board[6, i] = new Piece(PieceType.Pawn, PlayerColor.Black);
            }
        }

        /// <summary>
        /// Apply a move to the board
        /// </summary>
        /// <param name="m">Move to execute</param>
        public void ApplyMove(Move m)
        {
            m.Capture = this[m.To];
            this[m.To] = this[m.From];
            this[m.From] = null;
        }

        /// <summary>
        /// Undo a move. Sherlock says: "This may require that the move was previously applied."
        /// </summary>
        /// <param name="m">The move</param>
        public void UndoMove(Move m)
        {
            this[m.From] = this[m.To];
            this[m.To] = m.Capture;
        }

        /// <summary>
        /// If the move is a castling move, find the tower and move it too
        /// </summary>
        /// <param name="m">The move</param>
        public void DoCastling(Move m)
        {
            if (this[m.From].Type == PieceType.King)
            {
                int XDelta, YDelta;
                int XDir, YDir;

                m.AbsDelta(out XDelta, out YDelta);
                m.Direction(out XDir, out YDir);

                if (XDelta == 2 && YDelta == 0) // is a good castling move
                {
                    int TowerColumn = (XDir == 1 ? 7 : (XDir == -1 ? 0 : 666));
                    Move TowerMove = new Move(TowerColumn, m.From.Y, m.To.X - XDir, m.From.Y, "");
                    ApplyMove(TowerMove);
                }
            }
        }
        
        /// <summary>
        /// Update teh booleans of this class
        /// </summary>
        /// <param name="m">Move to take cues from</param>
        public void UpdateCastlingStatus(Move m)
        {
            if (this[m.From]._Color == PlayerColor.White)
            {
                if (m.From == _WhiteKing)
                {
                    _WhiteCanCastleShort = false;
                    _WhiteCanCastleLong = false;
                }
                if (m.From == _WhiteRookShort)
                    _WhiteCanCastleShort = false;
                if (m.From == _WhiteRookLong)
                    _WhiteCanCastleLong = false;
            }
            else
            {
                if (m.From == _BlackKing)
                {
                    _BlackCanCastleShort = false;
                    _BlackCanCastleLong = false;
                }
                if (m.From == _BlackRookShort)
                    _BlackCanCastleShort = false;
                if (m.From == _BlackRookLong)
                    _BlackCanCastleLong = false;
            }
        }

        /// <summary>
        /// Default property by x and y
        /// </summary>
        /// <param name="y">Y Coordinate</param>
        /// <param name="x">X Coordinate</param>
        /// <returns>Piece object reference</returns>
        public Piece this[int y, int x]
        {
            get
            {
                return _Board[y, x];
            }
            set
            {
                _Board[y, x] = value;
            }
        }

        /// <summary>
        /// Default property by coordinate
        /// </summary>
        /// <param name="c">Coordinate</param>
        /// <returns>Piece object reference</returns>
        public Piece this[Coordinate c]
        {
            get
            {
                return this[c.Y, c.X];
            }
            set
            {
                this[c.Y, c.X] = value;
            }
        }

        /// <summary>
        /// Status of white short castling ability
        /// </summary>
        public bool WhiteCanCastleShort
        {
            get
            {
                return _WhiteCanCastleShort;
            }
        }

        /// <summary>
        /// Status of white long castling ability
        /// </summary>
        public bool WhiteCanCastleLong
        {
            get
            {
                return _WhiteCanCastleLong;
            }
        }

        /// <summary>
        /// Status of black short castling ability
        /// </summary>
        public bool BlackCanCastleShort
        {
            get
            {
                return _BlackCanCastleShort;
            }
        }

        /// <summary>
        /// Status of black long castling ability
        /// </summary>
        public bool BlackCanCastleLong
        {
            get
            {
                return _BlackCanCastleLong;
            }
        }

        /// <summary>
        /// Count all occurences of pieces with a given type and color
        /// </summary>
        /// <param name="pt">Piece type to search for</param>
        /// <param name="pc">Player color to search for</param>
        /// <returns>Count of ocurrences found</returns>
        private int CountPiecesOfType(PieceType pt, PlayerColor pc)
        {
            int Result = 0;
            for (int y = 0; y < 8; y++)
                for (int x = 0; x < 8; x++)
                    if ((_Board[y, x] != null) 
                            && (_Board[y, x].Type == pt) 
                            && (_Board[y, x].Color == pc))
                        Result++;
            return Result;
        }

        /// <summary>
        /// Return an array of coordinates of all pieces that match given criteria
        /// </summary>
        /// <param name="pt">Piece type</param>
        /// <param name="pc">Player color</param>
        /// <returns>Array of Coordinate objects</returns>
        public Coordinate[] FindPiecesOfType(PieceType pt, PlayerColor pc)
        {
            int Count = CountPiecesOfType(pt, pc);
            int Index = 0;
            Coordinate[] Result = new Coordinate[Count];
            for (int y = 0; y < 8; y++)
                for (int x = 0; x < 8; x++)
                    if ((_Board[y, x] != null) 
                            && (_Board[y, x].Type == pt) 
                            && (_Board[y, x].Color == pc))
                        Result[Index++] = new Coordinate(x, y);
            return Result;
        }

        /// <summary>
        /// Return a list of pieces between coordinates A and B
        /// </summary>
        /// <param name="A">Start coordinate</param>
        /// <param name="B">End coordinates</param>
        /// <returns>Piece array; IIRC this array is 1 too long - sort of latent bug</returns>
        public Piece[] ReturnPiecesBetween(Coordinate A, Coordinate B)
        {
            int XDelta, YDelta;
            int XDir, YDir;

            // find differences and directions of coordinates
            B.AbsDelta(A, out XDelta, out YDelta);
            B.Direction(A, out XDir, out YDir);

            // allocate an array
            Piece[] Result = null;
            if (XDelta == 0)
                Result = new Piece[YDelta];
            if (YDelta == 0)
                Result = new Piece[XDelta];
            if (XDelta == YDelta)
                Result = new Piece[XDelta];

            //
            int X = A.X + XDir;
            int Y = A.Y + YDir;
            int Index = 0;

            //
            while (((XDir == 0) || ((XDir == 1) && (X < B.X)) || ((XDir == -1) && (X > B.X)))
                && ((YDir == 0) || ((YDir == 1) && (Y < B.Y)) || ((YDir == -1) && (Y > B.Y)))
            )   
            { 
                Result[Index++] = _Board[Y, X]; 

                X += XDir;
                Y += YDir;
            }

            return Result;
        }
    }
}
