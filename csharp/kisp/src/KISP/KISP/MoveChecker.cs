using System;
using System.Collections.Generic;
using System.Text;

namespace KISP.Chess
{
    // Class that takes a functional approach to checking moves for validity
    class MoveChecker
    {
        private ChessGame _ChessGame;
        private Board _Board;

        /// <summary>
        /// Reference to chess game, mostly for chess board and some booleans
        /// </summary>
        /// <param name="chessgame"></param>
        public MoveChecker(ChessGame chessgame)
        {
            _ChessGame = chessgame;
            _Board = _ChessGame.Board;
           
        }

        /// <summary>
        /// Check if a move is legal: it must be ANY move, be possible for that piece, not be
        /// obstructed by another piece, it must be a legal capture (if any) and must relieve a 
        /// check condition (if any).
        /// </summary>
        /// <param name="m">Move object</param>
        /// <returns>Truee if the move is OK</returns>
        public bool IsLegalMove(Move m)
        {
            return IsAnyMoveAtAll(m)
                && IsLegalForPieceType(m)
                && IsUnobstructed(m)
                && IsLegalCapture(m)
                && RelievesCheck(m);
        }

        /// <summary>
        /// Returns true if the begin and end coordinates are not equal
        /// </summary>
        /// <param name="m">Move</param>
        /// <returns>True if it's a move</returns>
        private bool IsAnyMoveAtAll(Move m)
        {
            int XDelta;
            int YDelta;

            m.AbsDelta(out XDelta, out YDelta);

            return XDelta + YDelta > 0;
        }

        /// <summary>
        /// Returns true if move is legal for piece type
        /// </summary>
        /// <param name="m">Move</param>
        /// <returns>True if move is possible for piece</returns>
        private bool IsLegalForPieceType(Move m)
        {
            int XDelta;
            int YDelta;

            m.AbsDelta(out XDelta, out YDelta);
            
            switch (_Board[m.From].Type) // TODO bad OO?
            {
                case PieceType.King:
                    {
                        return IsLegalForKing(m);
                    }
                case PieceType.Queen:
                    {
                        return IsLegalForQueen(XDelta, YDelta);
                    }
                case PieceType.Rook:
                    {
                        return IsLegalForRook(XDelta, YDelta);
                    }
                case PieceType.Bishop:
                    {
                        return IsLegalForBishop(XDelta, YDelta);
                    }
                case PieceType.Knight:
                    {
                        return IsLegalForKnight(XDelta, YDelta);
                    }
                case PieceType.Pawn:
                    {
                        return IsLegalForPawn(m);
                    }
                default:
                    {
                        return false;
                    }
            }
        }

        /// <summary>
        /// Check if move is legal for king
        /// </summary>
        /// <param name="m">Move</param>
        /// <returns>True if m is a legal king move</returns>
        private bool IsLegalForKing(Move m)
        {
            int XDelta;
            int YDelta;

            m.AbsDelta(out XDelta, out YDelta);

            return 
                ((XDelta <= 1) && (YDelta <= 1))
                || IsLegalCastling(m);
        }

        /// <summary>
        /// Return true if m is a legal castling move for a king!
        /// </summary>
        /// <param name="m">Move</param>
        /// <returns>True if m is a legal castling move</returns>
        private bool IsLegalCastling(Move m)
        {
            int XDelta;
            int YDelta;

            int XDir;
            int YDir;

            m.AbsDelta(out XDelta, out YDelta);
            m.Direction(out XDir, out YDir);

            // king may not now be in check
            if (KingIsNowInCheck())
                return false;

            // king can not be in check along the way
            Move CastlePart = new Move(m.From.X, m.From.Y, m.From.X + XDir, m.From.Y, "");
            _Board.ApplyMove(CastlePart);
            bool IsInCheck = KingIsNowInCheck();
            _Board.UndoMove(CastlePart);
            if (IsInCheck)
                return false;

            // may not have moved king and rooks
            if (_Board[m.From].Color == PlayerColor.White)
            {
                if (XDir == 1)
                    return _Board.WhiteCanCastleShort;
                if (XDir == -1)
                    return _Board.WhiteCanCastleLong;
            }
            else
            {
                if (XDir == 1)
                    return _Board.BlackCanCastleShort;
                if (XDir == -1)
                    return _Board.WhiteCanCastleLong;
            }
            return false;
        }

        /// <summary>
        /// Check if move is legal for rook
        /// </summary>
        /// <param name="XDelta">Horizontal distance travelled</param>
        /// <param name="YDelta">Vertical distance travelled</param>
        /// <returns>True if m is a legal rook move</returns>
        private bool IsLegalForRook(int XDelta, int YDelta)
        {
            return (XDelta == 0) || (YDelta == 0);
        }

        /// <summary>
        /// Check if move is legal for bishop
        /// </summary>
        /// <param name="XDelta">Horizontal distance travelled</param>
        /// <param name="YDelta">Vertical distance travelled</param>
        /// <returns></returns>
        private bool IsLegalForBishop(int XDelta, int YDelta)
        {
            return XDelta == YDelta;
        }

        /// <summary>
        /// Check if move is legal for knight
        /// </summary>
        /// <param name="XDelta">Horizontal distance travelled</param>
        /// <param name="YDelta">Vertical distance travelled</param>
        /// <returns></returns>
        private bool IsLegalForKnight(int XDelta, int YDelta)
        {
            return ((XDelta == 1) && (YDelta == 2))
                || ((XDelta == 2) && (YDelta == 1));
        }

        /// <summary>
        /// Check if move is legal for queen
        /// </summary>
        /// <param name="XDelta"></param>
        /// <param name="YDelta"></param>
        /// <returns></returns>
        private bool IsLegalForQueen(int XDelta, int YDelta)
        {
            return IsLegalForRook(XDelta, YDelta) 
                || IsLegalForBishop(XDelta, YDelta);
        }

        /// <summary>
        /// Check if move is legal for pawn
        /// </summary>
        /// <param name="m"></param>
        /// <returns></returns>
        private bool IsLegalForPawn(Move m)
        {
            return IsLegalPawnMove(m)
                || IsLegalPawnCapture(m);
                // IsLegalEnPassentCapture(m);
        }

        /// <summary>
        /// Return true if the move is a legal pawn move
        /// </summary>
        /// <param name="m"></param>
        /// <returns></returns>
        private bool IsLegalPawnMove(Move m)
        {
            int XDelta;
            int YDelta;

            m.Delta(out XDelta, out YDelta);

            if (_ChessGame.MyColor == PlayerColor.White)
                return (m.From.Y == 1)
                    ? (XDelta == 0) && (YDelta > 0) && (YDelta <= 2) && (_Board[m.To] == null)
                    : (XDelta == 0) && (YDelta > 0) && (YDelta <= 1) && (_Board[m.To] == null);
            else
                return (m.From.Y == 6)
                    ? (XDelta == 0) && (YDelta < 0) && (YDelta <= 2) && (_Board[m.To] == null)
                    : (XDelta == 0) && (YDelta < 0) && (YDelta <= 1) && (_Board[m.To] == null);
        }

        /// <summary>
        /// Return true if the move is a legal capture
        /// </summary>
        /// <param name="m"></param>
        /// <returns></returns>
        private bool IsLegalPawnCapture(Move m)
        {
            int XDelta;
            int YDelta;

            m.Delta(out XDelta, out YDelta);

            if (_ChessGame.MyColor == PlayerColor.White)
                return (Math.Abs(XDelta) == 1) 
                    && (YDelta == 1) 
                    && (_Board[m.To] != null) 
                    && (_Board[m.To].Color != _ChessGame.MyColor);
            else
                return (Math.Abs(XDelta) == 1)
                    && (YDelta == -1)
                    && (_Board[m.To] != null)
                    && (_Board[m.To].Color != _ChessGame.MyColor);
        }

        /// <summary>
        /// Check if no pieces are between the start and end point of the move
        /// </summary>
        /// <param name="m"></param>
        /// <returns></returns>
        private bool IsUnobstructed(Move m)
        {
            if (_Board[m.From].Type == PieceType.Knight)
                return true; // can't obstruct knight

            Piece[] Pieces = _Board.ReturnPiecesBetween(m.From, m.To);

            foreach (Piece p in Pieces)
                if (p != null)
                    return false;
            return true;
        }

        /// <summary>
        /// Return true if move is a legal capture or a move to an empty spot
        /// </summary>
        /// <param name="m"></param>
        /// <returns></returns>
        private bool IsLegalCapture(Move m)
        {
            Piece p = _Board[m.To];
            if (p == null)
                return true;
            else
                return p.Color != _ChessGame.MyColor;
        }

        /// <summary>
        /// Return true if move relieves check condition or true if there was no check condition
        /// to begin with
        /// </summary>
        /// <param name="m"></param>
        /// <returns></returns>
        private bool RelievesCheck(Move m)
        {
            //if (! KingIsNowInCheck())
            //    return true;
            //else
            //    return ! KingWillBeInCheck(m);
            return !KingWillBeInCheck(m);
        }

        /// <summary>
        /// Returns true if the king is in check in this board posiiton
        /// </summary>
        /// <returns></returns>
        private bool KingIsNowInCheck()
        {
            Coordinate[] King = _Board.FindPiecesOfType(PieceType.King, _ChessGame.MyColor);
            return KingInCheckByRook(King[0])
                || KingInCheckByKnight(King[0])
                || KingInCheckByBishop(King[0])
                || KingInCheckByQueen(King[0])
                || KingInCheckByPawn(King[0]);
        }

        /// <summary>
        /// Returns true if the king will be in check at the end of the move
        /// </summary>
        /// <param name="m"></param>
        /// <returns></returns>
        private bool KingWillBeInCheck(Move m)
        {
            _Board.ApplyMove(m);
            bool result = KingIsNowInCheck();
            _Board.UndoMove(m);
            return result;
        }

        /// <summary>
        /// Returns true if king is in check because of a rook
        /// </summary>
        /// <param name="King"></param>
        /// <returns></returns>
        private bool KingInCheckByRook(Coordinate King)
        {
            Coordinate[] Rooks = _Board.FindPiecesOfType(PieceType.Rook, _ChessGame.OtherColor);
            foreach (Coordinate Rook in Rooks)
            {
                if ((King.X == Rook.X) || (King.Y == Rook.Y))
                {
                    bool Result = true;
                    Piece[] Pieces = _Board.ReturnPiecesBetween(King, Rook);
                    foreach (Piece p in Pieces)
                        if (p != null)
                            Result = false;
                    if (Result)
                        return Result;
                }
            }
            return false;
        }

        /// <summary>
        /// Returns true if king in check because of a bishop
        /// </summary>
        /// <param name="King"></param>
        /// <returns></returns>
        private bool KingInCheckByBishop(Coordinate King)
        {
            int XDelta;
            int YDelta;
            Coordinate[] Bishops = _Board.FindPiecesOfType(PieceType.Bishop, _ChessGame.OtherColor);
            foreach (Coordinate Bishop in Bishops)
            {
                King.AbsDelta(Bishop, out XDelta, out YDelta);
                if (XDelta == YDelta)
                {
                    bool Result = true;
                    Piece[] Pieces = _Board.ReturnPiecesBetween(King, Bishop);
                    foreach (Piece p in Pieces)
                        if (p != null)
                            Result = false;
                    if (Result)
                        return Result;
                }
            }
            return false;
        }

        /// <summary>
        /// Returns true if king in check because of a queen
        /// </summary>
        /// <param name="King"></param>
        /// <returns></returns>
        private bool KingInCheckByQueen(Coordinate King)
        {
            int XDelta;
            int YDelta;
            Coordinate[] Queens = _Board.FindPiecesOfType(PieceType.Queen, _ChessGame.OtherColor);
            foreach (Coordinate Queen in Queens)
            {
                King.AbsDelta(Queen, out XDelta, out YDelta);
                
                // check diagonals
                if (XDelta == YDelta)
                {
                    bool Result = true;
                    Piece[] Pieces = _Board.ReturnPiecesBetween(King, Queen);
                    foreach (Piece p in Pieces)
                        if (p != null)
                            Result = false;
                    if (Result)
                        return Result;
                }

                // check ranks and files
                if ((King.X == Queen.X) || (King.Y == Queen.Y))
                {
                    bool Result = true;
                    Piece[] Pieces = _Board.ReturnPiecesBetween(King, Queen);
                    foreach (Piece p in Pieces)
                        if (p != null)
                            Result = false;
                    if (Result)
                        return Result;
                }
            }
            return false;
        }

        /// <summary>
        /// Returns true if king in check because of a knight
        /// </summary>
        /// <param name="King"></param>
        /// <returns></returns>
        private bool KingInCheckByKnight(Coordinate King)
        {
            int XDelta;
            int YDelta;
            Coordinate[] Knights = _Board.FindPiecesOfType(PieceType.Knight, _ChessGame.OtherColor);
            foreach (Coordinate Knight in Knights)
            {
                King.AbsDelta(Knight, out XDelta, out YDelta);
                if (((XDelta == 1) && (YDelta == 2))
                    || ((XDelta == 2) && (YDelta == 1)))
                    return true;
            }
            return false;
        }

        /// <summary>
        /// Returns true if king in check because of a pawn
        /// </summary>
        /// <param name="King"></param>
        /// <returns></returns>
        private bool KingInCheckByPawn(Coordinate King)
        {
            int XDelta;
            int YDelta;
            Coordinate[] Pawns = _Board.FindPiecesOfType(PieceType.Pawn, _ChessGame.OtherColor);
            foreach (Coordinate Pawn in Pawns)
            {
                King.Delta(Pawn, out XDelta, out YDelta);
                if (_ChessGame.MyColor == PlayerColor.White)
                {
                    if (Math.Abs(XDelta) == 1 && YDelta == -1)
                        return true;
                }
                else
                {
                    if (Math.Abs(XDelta) == 1 && YDelta == 1)
                        return true;
                }
            }
            return false;
        }
    }
}
