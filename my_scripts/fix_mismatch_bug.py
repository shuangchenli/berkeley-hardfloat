#!/usr/bin/python
import os
import subprocess

objects_fma = [\
 'MulAddRecF16_add',\
 'MulAddRecF16_mul',\
 'MulAddRecF16',\
 'MulAddRecBF16_add',\
 'MulAddRecBF16_mul',\
 'MulAddRecBF16',\
 'MulAddRecF32_add',\
 'MulAddRecF32_mul',\
 'MulAddRecF32',\
 'MulAddRecF64_add',\
 'MulAddRecF64_mul',\
 'MulAddRecF64'\
]

for ob in objects_fma:
  files = '../results/' + ob + '/ValExec_' + ob + '.v'
  r = subprocess.check_output(['grep -inr \"] mulAddResult;\" ' + files], shell=True)
  if(r == []):
    continue
  else:
    print r
    wire = int(r.split('[')[1].split(':')[0])
    print wire
    os.system("sed -ie 's/wire\[" + str(wire) + ":0\] mulAddResult;/wire\[" + str(wire-1) + ":0\] mulAddResult;/g' " + files)
    
