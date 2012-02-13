object frmArmsDealer: TfrmArmsDealer
  Left = 730
  Top = 428
  Width = 391
  Height = 357
  Caption = 'arms dealer'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  OnShow = FormShow
  PixelsPerInch = 96
  TextHeight = 13
  object ListView1: TListView
    Left = 24
    Top = 104
    Width = 337
    Height = 209
    Columns = <
      item
        Caption = 'Name'
      end
      item
        Caption = 'Price'
      end
      item
        Caption = 'Damage'
        Width = 60
      end
      item
        Caption = 'Ammo'
      end>
    TabOrder = 0
    ViewStyle = vsReport
    OnClick = ListView1Click
  end
  object btnViolence: TButton
    Left = 280
    Top = 16
    Width = 75
    Height = 25
    Caption = '&Violence'
    TabOrder = 1
    OnClick = btnViolenceClick
  end
  object btnClose: TButton
    Left = 280
    Top = 48
    Width = 75
    Height = 25
    Caption = '&Close'
    TabOrder = 2
    OnClick = btnCloseClick
  end
end
