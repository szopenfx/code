
#include "dotlistbox.h"

/*
 * Construct listbox for showing points
 *  *parent - parent widget
 *  *p [subject] - points object
 */
DotListBox::DotListBox(QWidget *parent, Points *p)
  : QListBox(parent, "mylistbox")
  , points(p)
{
  setMinimumWidth(150);
  setMaximumWidth(200);
  points->Attach(*this);
}

/*
 * Destructor
 */
DotListBox::~DotListBox()
{
  points->Detach(*this);
}

/*
 * Update [observer] - re-fill listbox
 */
void DotListBox::Update()
{
  clear();
  for(int i = 0; i < points->Count(); i++)
  {
    QString coord = QString("%1,%2")
      .arg((*points)[i].x)
      .arg((*points)[i].y);
    insertItem(coord);
  }
}
