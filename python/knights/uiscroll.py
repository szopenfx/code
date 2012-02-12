#!/usr/bin/python
import Tkinter as tk
root = tk.Tk()

canvas = tk.Canvas(root, width=100, height=100, scrollregion=(0,0,1000,1000))

scrolly = tk.Scrollbar(root, orient='v')
scrollx = tk.Scrollbar(root, orient='h')

scrolly.pack(fill='y', side='right')
canvas.pack()
scrollx.pack(fill='x')

canvas['xscrollcommand'] = scrollx.set ; scrollx['command'] = canvas.xview
canvas['yscrollcommand'] = scrolly.set ; scrolly['command'] = canvas.yview

canvas.create_line(0,0,100,100)

root.mainloop()
