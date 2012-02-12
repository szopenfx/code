
#include "dotwidget.h"

/*
 * Construct dot widget
 *  *parent - parent widget
 *  *p - points object [subject]
 */
DotWidget::DotWidget(QWidget *parent, Points *p)
  : QWidget(parent)
  , points(p)
{
  setMinimumWidth(400);
  setMinimumHeight(300);
  points->Attach(*this);
}

/*
 * Destructor
 */
DotWidget::~DotWidget()
{
  points->Detach(*this);
}

/*
 * Update [observer] - re-paint dots
 */
void DotWidget::Update()
{
  repaint();
}

/*
 * paintEvent - paint the canvas
 *  *qpe - unused paint event
 */
void DotWidget::paintEvent(QPaintEvent *qpe)
{
  QPainter painter(this);
  painter.setPen(color);
  painter.eraseRect(0, 0, width(), height());
  for(int i = 0; i < points->Count(); i++)
  {
    Point &point = (*points)[i];
    painter.drawEllipse(point.x, point.y, 2, 2);
  }
}

/*
 * mouseReleaseEvent - mouse was clicked and released
 *  *qme - mouse event
 */
void DotWidget::mouseReleaseEvent(QMouseEvent *qme)
{
  points->Add(qme->x(), qme->y());
}
