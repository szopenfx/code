
#ifndef POINTS_H
#define POINTS_H

#include <vector>
#include "subject.h"

class Point
{
  public:
    Point(int px, int py) : x(px), y(py) {};
    int x;
    int y;
};

class Points : public Subject
{
  private:
    std::vector<Point> points;
  public:
    int Count();
    void Add(int x, int y);
    void Del(int index);
    Point &operator[](int index);
};

#endif
