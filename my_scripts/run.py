#!/usr/bin/python

import os
objects_test = ['MulAddRecF16_add']
objects_int = [\
 'ADD_INT8',\
 'MUL_INT8',\
 'FMA_INT8',\
 'ADD_INT16',\
 'MUL_INT16',\
 'FMA_INT16',\
 'ADD_INT32',\
 'MUL_INT32',\
 'FMA_INT32'\
] 
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

objects_full = [\
 'f16FromRecF16',\
 'f32FromRecF32',\
 'f64FromRecF64',\
 'UI32ToRecF16',\
 'UI32ToRecF32',\
 'UI32ToRecF64',\
 'UI64ToRecF16',\
 'UI64ToRecF32',\
 'UI64ToRecF64',\
 'I32ToRecF16',\
 'I32ToRecF32',\
 'I32ToRecF64',\
 'I64ToRecF16',\
 'I64ToRecF32',\
 'I64ToRecF64',\
 'RecF16ToUI32',\
 'RecF16ToUI64',\
 'RecF32ToUI32',\
 'RecF32ToUI64',\
 'RecF64ToUI32',\
 'RecF64ToUI64',\
 'RecF16ToI32',\
 'RecF16ToI64',\
 'RecF32ToI32',\
 'RecF32ToI64',\
 'RecF64ToI32',\
 'RecF64ToI64',\
 'RecF16ToRecF32',\
 'RecF16ToRecF64',\
 'RecF32ToRecF16',\
 'RecF32ToRecF64',\
 'RecF64ToRecF16',\
 'RecF64ToRecF32',\
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
 'MulAddRecF64',\
 'DivSqrtRecF16_small_div',\
 'DivSqrtRecF16_small_sqrt',\
 'DivSqrtRecF32_small_div',\
 'DivSqrtRecF32_small_sqrt',\
 'DivSqrtRecF64_div',\
 'DivSqrtRecF64_sqrt',\
 'CompareRecF16_lt',\
 'CompareRecF16_le',\
 'CompareRecF16_eq',\
 'CompareRecF32_lt',\
 'CompareRecF32_le',\
 'CompareRecF32_eq',\
 'CompareRecF64_lt',\
 'CompareRecF64_le',\
 'CompareRecF64_eq',\
]

def verilog_gen(objects):
# chisel verilog gen
  for ob in objects:
    print("sbt -DchiselVersion=2.2.38 \"run " + ob + " --targetDir ./results/" + ob + " --backend v\"")
    os.system("sbt -DchiselVersion=2.2.38 \"run " + ob + " --targetDir ./results/" + ob + " --backend v\"")

def verilog_gen_int(objects, width_w, width_act, width_acc):
# chisel verilog gen
  for ob in objects:
    for w in width_w:
      for act in width_act:
        for acc in width_acc:
          print("sbt -DchiselVersion=2.2.38 \"run " + ob + " " + str(w) + " " + str(act) + " " + str(acc) + " --targetDir ./results/" + ob + str(w) + str(act) + str(acc) + " --backend v\"")
          os.system("sbt -DchiselVersion=2.2.38 \"run " + ob + " " + str(w) + " " + str(act) + " " + str(acc) + " --targetDir ./results/" + ob + str(w) + str(act) + str(acc) + " --backend v\"")

def run_dc(objects, tech):
# DC
  for ob in objects:
    print("======================================================")
    print("Run Synthesis with Design Compiler for <" + ob + ">...")
    print("======================================================")
    os.chdir("./results/" + ob)
    os.system("mkdir -p result")
    os.system("mkdir -p synthesis")
    os.chdir("./synthesis")
    if(tech == 15):
      os.system("dc_shell -x \"set top_module \\\"ValExec_" + ob + "\\\"\" -f ../../../my_scripts/design_compiler_15.tcl | tee \"run.log\"")
    else:
      os.system("dc_shell -x \"set top_module \\\"ValExec_" + ob + "\\\"\" -f ../../../my_scripts/design_compiler.tcl | tee \"run.log\"")
    os.chdir("../../../")
    print("======================================================")
    print("         <" + ob + "> Synthesis Done.")
    print("======================================================")
def clean_dc(objects):
  for ob in objects:
    print "Clean All Design Compiler Results ..."
    os.chdir("./results/" + ob)
    os.system("rm -rf result")
    os.system("rm -rf synthesis")
    os.system("rm -f *.log *.mr *.svf *.pvl *.syn")
    os.chdir("../../")
def clean_dc_int(objects):
  for ob in objects:
    for w in width_w:
      for act in width_act:
        for acc in width_acc:
          print "Clean All Design Compiler Results ..."
          os.chdir("./results/" + ob + str(w) + str(act) + str(acc))
          os.system("rm -rf result")
          os.system("rm -rf synthesis")
          os.system("rm -f *.log *.mr *.svf *.pvl *.syn")
          os.chdir("../../")


def main():
  #verilog_gen(objects_full)
  #clean_dc(objects_full)
  #run_dc(objects_full, 45)
  #verilog_gen(objects_fma)
  clean_dc(objects_fma)
  #run_dc(objects_fma, 45)
  #run_dc(objects_test, 15)
  #verilog_gen(objects_int)
  clean_dc(objects_int)
  #run_dc(objects_test, 15)
  run_dc(objects_fma, 15)
  run_dc(objects_int, 15)
if __name__ == "__main__":
    main()  
