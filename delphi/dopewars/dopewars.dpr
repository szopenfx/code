program dopewars;
{DOPE WARS ß€†Æ v0.8 (c) 2003 by JOoSt Molenaar}

uses
  Forms,
  formMain in 'formMain.pas' {Main},
  formAmount in 'formAmount.pas' {frmAmount},
  formArmsDealer in 'formArmsDealer.pas' {frmArmsDealer},
  formBank in 'formBank.pas' {frmBank},
  formDoctor in 'formDoctor.pas' {frmDoctor},
  formHighScores in 'formHighScores.pas' {frmHighScores},
  formPrices in 'formPrices.pas' {frmPrices},
  formViolence in 'formViolence.pas' {frmViolence},
  frameMenu in 'frameMenu.pas' {fraMenu: TFrame},
  globals in 'globals.pas',
  classArmsDealer in 'classArmsDealer.pas',
  classBankAccount in 'classBankAccount.pas',
  classCheats in 'classCheats.pas',
  classCity in 'classCity.pas',
  classDoctor in 'classDoctor.pas',
  classDopeWars in 'classDopeWars.pas',
  classDrug in 'classDrug.pas',
  classDrugList in 'classDrugList.pas',
  classHighScores in 'classHighScores.pas',
  classHolster in 'classHolster.pas',
  classJacket in 'classJacket.pas',
  classLocation in 'classLocation.pas',
  classLuck in 'classLuck.pas',
  classMarket in 'classMarket.pas',
  classMessenger in 'classMessenger.pas',
  classPerson in 'classPerson.pas',
  classReplicator in 'classReplicator.pas',
  classScore in 'classScore.pas',
  classViolence in 'classViolence.pas',
  classWallet in 'classWallet.pas',
  classWeapon in 'classWeapon.pas',
  classPeople in 'classPeople.pas',
  classPlayer in 'classPlayer.pas',
  classPimp in 'classPimp.pas',
  formBrothel in 'formBrothel.pas' {frmBrothel},
  classBank in 'classBank.pas',
  classLoanShark in 'classLoanShark.pas',
  classGroup in 'classGroup.pas',
  formOuterSpace in 'formOuterSpace.pas' {frmOuterSpace},
  classTrip in 'classTrip.pas';

{$R *.res}

begin
  Application.Initialize;
  Application.CreateForm(TMain, Main);
  Application.CreateForm(TfrmAmount, frmAmount);
  Application.CreateForm(TfrmBank, frmBank);
  Application.CreateForm(TfrmPrices, frmPrices);
  Application.CreateForm(TfrmHighScores, frmHighScores);
  Application.CreateForm(TfrmArmsDealer, frmArmsDealer);
  Application.CreateForm(TfrmViolence, frmViolence);
  Application.CreateForm(TfrmDoctor, frmDoctor);
  Application.CreateForm(TfrmBrothel, frmBrothel);
  Application.CreateForm(TfrmOuterSpace, frmOuterSpace);
  Application.Run;
end.
