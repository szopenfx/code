
#ifndef DOTLISTBOX_H
#define DOTLISTBOX_H

#include <qlistbox.h>

#include "observer.h"
#include "points.h"

class DotListBox : public QListBox, public Observer
{
  private:
    Points *points;
  public:
    DotListBox(QWidget *parent, Points *p = NULL);
    virtual ~DotListBox();
    void Update();
};

#endif
