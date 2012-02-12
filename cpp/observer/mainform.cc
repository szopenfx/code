
#include "mainform.h"
#include "moc_mainform.h"

/*
 * Construct main form
 *  argc - argument count
 *  *argv[] - argument vector
 */
MainForm::MainForm(int argc, char *argv[])
  : points()
  , app(argc, argv)
  , hbox(0)
  , vbox(&hbox)
  , listbox(&vbox, &points)
  , remove("&Remove", &vbox)
  , color("&Color", &vbox)
  , quit("&Quit", &vbox)
  , dots(&hbox, &points)
{
  remove.setEnabled(false);
  points.Attach(*this);

  QObject::connect(
    &quit, SIGNAL(clicked()),
    &app, SLOT(quit()));

  QObject::connect(
    &remove, SIGNAL(clicked()),
    this, SLOT(removeClicked()));

  QObject::connect(
    &color, SIGNAL(clicked()),
    this, SLOT(colorClicked()));
    
  QObject::connect(
    &listbox, SIGNAL(currentChanged(QListBoxItem*)),
    this, SLOT(currentChanged(QListBoxItem*)));
}

/*
 * Destructor
 */
MainForm::~MainForm()
{
  points.Detach(*this);
}

/*
 * Execute - show and run application
 * returns:
 *  exit code
 */
int MainForm::Execute()
{
  app.setMainWidget(&hbox);
  hbox.show();
  return app.exec();
}

/*
 * Update [observer] - update form because points changed
 */
void MainForm::Update()
{
  remove.setEnabled(false);
}

/*
 * currentChanged [slot] - listbox selection changed
 *  item - selected item
 */
void MainForm::currentChanged(QListBoxItem *item)
{
  remove.setEnabled(listbox.index(item) > -1);
}

/*
 * removeClicked [slot] - remove button was clicked
 */
void MainForm::removeClicked()
{
  int item = listbox.currentItem();
  if(item > -1)
    points.Del(item);
}

/*
 * colorClicked [slot] - color button was clicked
 */
void MainForm::colorClicked()
{
  dots.color = QColorDialog::getColor(dots.color, &hbox);
  dots.Update();
}

/*
 * main
 */
int main(int argc, char *argv[])
{
  MainForm mainform(argc, argv);
  return mainform.Execute();
}
