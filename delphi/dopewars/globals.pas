unit globals;

interface

type
  dlgrc = record
    cap: string;
    msg: string;
    amt: string;
  end;

const
  {VALUTA SIGN}
  VALUTUM                 = {#131}'ƒ'; // NLG

  {DEFAULT PLAYER NAME}
  sDefaultName            = 'Whatever';

  {MESSAGES}
  sWelcomeMessage         = 'Welcome to DOPE wars.';
  sGoto                   = 'You travel to the %s.';
  sUseDrugOD              = 'O.D.! You''re DEAD, FOOL!';
  sUseDrugFucked          = 'Are you FUCKED!';
  sBuyWeaponNoMoney       = 'A %s is too expensive for you.';
  sBuyWeaponBought        = 'Added a nice %s to your arsenal.';
  sNoDemand               = 'Nobody wants your %s.';
  sGeneralMessage         = '%d: %s';
  sBankWithdrawDebtLimit  = 'You''ve reached your debt limit.';
  sBankDepositNoMoney     = 'You don''t have money to deposit.';
  sKidneyDeath            = 'You really need kidneys.';
  sDoctorBroke            = 'Unfortunately, the doctor is out of cash.';
            // ehhhm bank account?

  {STATI}
{  sp1 = '%s';
  sp2 = 'Health: %d%%';
  sp3 = VALUTUM;}

  {LABELS}
  sName                   = '%s';
  sHP                     = 'Health: %d%%';
  sCash                   = VALUTUM + ' %d,-';
  sJacket                 = 'Space in jacket: %d/%d';
  sBank                   = 'Bank: ' + VALUTUM + ' %d,-';
  sDaysLeft               = 'Day %d of 31';
  sLocation               = 'Location: %s';
  sBitches                = 'Bitches: %d';
  sKidneys                = 'Kidneys: %d';
  sWeapons                = 'Weapon: %s';
  sBankLblCredit          = 'Credit: ' + VALUTUM + ' %d,-';
  sBankLblInterest        = 'Interest rate is %d%%';
  sDoctorLblHP            = 'Your health is %d%%';
  sDoctorLblCost          = 'The doctor charges ' + VALUTUM + ' %d,- per %%';
  sDoctorKidneySell       = 'Doctor will put a kidney in you for ' + VALUTUM + ' %d,-.';
  sDoctorKidneyBuy        = 'Doctor will buy your kidney for ' + VALUTUM + ' %d,-.';
  sViolenceWeapon         = 'Current weapon: %s (%d bullets)';
  sViolenceNoWeapon       = 'Current weapon: <none>';
  sBrothelPimpName        = '%s in the motherfucking HOUSE!';
  sBrothelBuy             = 'You can buy a bitch for ' + VALUTUM + ' %d,-';
  sBrothelSell            = 'You can sell a bitch for ' + VALUTUM + ' %d,-';
  sBrothelHave            = 'You have %d hoes and room for %d units of drugs.';

  {CLOSE ROUTINE}
  CloseMsg: array[1..7] of PAnsiChar = (
    'Are you sure?',
    'Are you sure you''re sure?',
    'Did you mean the opposite?',
    'Ever been rogered by a wildebeast?',
    'Suck my dick?',
    'o_o',
    'OK?'
  );
  CloseCap = 'Exit Dope Wars';
  ClosePlay = 'Ah yea, play some more.';

  {DIALOGS}
  sChangeName: dlgrc = (
    cap: 'Change name';
    msg: 'You shall be known as:';
    amt: ''
  );
  sPlayAgain: dlgrc = (
    cap: 'New game';
    msg: 'Play again?';
    amt: ''
  );
  sBankWithdraw: dlgrc = (
    cap: 'Withdraw money';
    msg: 'You can withdraw ' + VALUTUM + ' %d.';
    amt: '';
  );
  sGetName: dlgrc = (
    cap: 'New game';
    msg: 'You shall be known as:';
    amt: '';
  );
  sUseDrug: dlgrc = (
    cap: 'Use drug';
    msg: 'You can inflict %d units of %s on yourself.';
    amt: '%d units is a waste of ' + VALUTUM + ' %d.'
  );
  sBuyDrug: dlgrc = (
    cap: 'Buy drug';
    msg: 'You can buy %d units of %s.';
    amt: '%d units will cost ' + VALUTUM + ' %d.'
  );
  sSellDrug: dlgrc = (
    cap: 'Sell drug';
    msg: 'You have %d units of %s.';
    amt: '%d units are worth ' + VALUTUM + ' %d.'
  );
  sGameOver: dlgrc = (
    cap: 'Game over, high score!';
    msg: 'Since you entered no name, you can enter one now.';
    amt: '';
  );
  sBankDeposit: dlgrc = (
    cap: 'Deposit money';
    msg: 'You have ' + VALUTUM + ' %d.';
    amt: '';
  );
  sDoctorHeal: dlgrc = (
    cap: 'Buy Hit Points';
    msg: 'You can heal %d hit points.';
    amt: '%d hit points cost ' + VALUTUM + ' %d.'
  );
  sBuyBitch: dlgrc = (
    cap: 'Buy bitch';
    msg: 'You can buy %d bitches.';
    amt: '%d bitches cost ' + VALUTUM + ' %d.'
  );
  sSellBitch: dlgrc = (
    cap: 'Sell bitch';
    msg: 'You have %d bitches.';
    amt: '%d bitches will earn you ' + VALUTUM + ' %d.'
  );
  sSellKidney: dlgrc = (
    cap: 'Sell kidney';
    msg: 'You can sell %d kidneys.';
    amt: '%d kidneys will earn you ' + VALUTUM + ' %d.'
  );
  sBuyKidney: dlgrc = (
    cap: 'Buy kidney';
    msg: 'You can buy %d kidneys.';
    amt: '%d kidneys will cost you ' + VALUTUM + ' %d.'
  );

implementation

end.
