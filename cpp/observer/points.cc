
#include "points.h"

int Points::Count()
{
  return points.size();
}

void Points::Add(int x, int y)
{
  points.push_back(Point(x, y));
  Notify();
}

void Points::Del(int index)
{
  /** /
  points.erase(&points[i]);
  / **/
  std::vector<Point>::iterator point;
  for(point = points.begin(); point != points.end(); point++)
    if(&(*point) == &points[index])
      break;
  if(point != points.end())
    points.erase(point);
  /**/
  Notify();
}

Point &Points::operator[](int index)
{
  return points[index];
}
