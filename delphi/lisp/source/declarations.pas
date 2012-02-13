unit declarations;

interface

type
  { ttNone is for TScanner, not used by TParesr }
  { ttLog is for TScanner; not used by TParser }
  TTokenType = ( ttNone, ttLParen, ttRParen, ttIdentifier, ttNumber );

const
  CTokenStr: array[TTokenType] of string =
    ( 'NONE', 'LEFT', 'RIGHT', 'IDENTIFIER', 'NUMBER' );

const
  CLetters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ=<>+-*/!?~%|&^';
  CWeirdLetters = '=<>+-*/!?~%|&^';
  CNumbers = '0123456789';
  CLParen = '(';
  CRParen = ')';
  CWhiteSpace = #9#10#13#32;
  CComment = ';';

type
  TToken = record
    type_: TTokenType;
    value: string;
    line: Integer;
  end;

implementation

end.
