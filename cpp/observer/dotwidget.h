
#ifndef DOTWIDGET_H
#define DOTWIDGET_H

#include <qwidget.h>
#include <qpainter.h>
#include <qcolor.h>

#include "observer.h"
#include "points.h"

class DotWidget : public QWidget, public Observer
{
  private:
    Points *points;
  public:
    QColor color;
    DotWidget(QWidget *parent, Points *p = NULL);
    virtual ~DotWidget();
    virtual void Update();
    void paintEvent(QPaintEvent *qpe);
    void mouseReleaseEvent(QMouseEvent *qme);
};

#endif
