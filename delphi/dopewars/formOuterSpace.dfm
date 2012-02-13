object frmOuterSpace: TfrmOuterSpace
  Left = 192
  Top = 106
  Width = 246
  Height = 261
  Caption = 'visit outer space'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object lblDrugs: TLabel
    Left = 0
    Top = 0
    Width = 238
    Height = 13
    Align = alTop
    Caption = 'Assemble your drug cocktail, space monkey:'
  end
  object cbxDrugs: TCheckListBox
    Left = 0
    Top = 13
    Width = 238
    Height = 180
    Align = alClient
    ItemHeight = 13
    TabOrder = 0
  end
  object Panel1: TPanel
    Left = 0
    Top = 193
    Width = 238
    Height = 41
    Align = alBottom
    BevelOuter = bvNone
    Caption = 'Panel1'
    TabOrder = 1
    object Button1: TButton
      Left = 160
      Top = 16
      Width = 75
      Height = 25
      Caption = 'OK'
      Default = True
      TabOrder = 0
      OnClick = Button1Click
    end
    object Button2: TButton
      Left = 80
      Top = 16
      Width = 75
      Height = 25
      Cancel = True
      Caption = 'Cancel'
      TabOrder = 1
      OnClick = Button2Click
    end
  end
end
