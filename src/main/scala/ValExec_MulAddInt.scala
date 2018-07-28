
/*============================================================================

This Chisel source file is part of a pre-release version of the HardFloat IEEE
Floating-Point Arithmetic Package, by John R. Hauser (with some contributions
from Yunsup Lee and Andrew Waterman, mainly concerning testing).

Copyright 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017 The Regents of the
University of California.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. Neither the name of the University nor the names of its contributors may
    be used to endorse or promote products derived from this software without
    specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS "AS IS", AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE
DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

=============================================================================*/

package hardfloat

import Chisel._
import scala.math.max

class ValExec_ADD(width_w: Int, width_a: Int, width_acc: Int) extends Module {
  val io = IO(new Bundle {
    //val weight_i  = Input(SInt(param_width_weight.W))
    val act_i     = Input(SInt(width = width_a))
    val acc_i     = Input(SInt(width = width_acc))
    //val mul_en    = Input(Bool())
    val add_en    = Input(Bool())
    val result_o  = Output(SInt(width = max(width_acc, width_a) + 1))
  })
  val mac = Module(new MAC (1, width_a, width_acc));
  mac.io.weight_i := SInt(0);
  mac.io.act_i := io.act_i;
  mac.io.acc_i := io.acc_i;
  mac.io.mul_en := false.B
  mac.io.add_en := io.add_en;
  io.result_o := mac.io.result_o;
}

class ValExec_MUL(width_w: Int, width_a: Int, width_acc: Int) extends Module {
  val io = IO(new Bundle {
    val weight_i  = Input(SInt(width = width_w))
    val act_i     = Input(SInt(width = width_a))
    //val acc_i     = Input(SInt(param_width_acc.W))
    val mul_en    = Input(Bool())
    //val add_en    = Input(Bool())
    val result_o  = Output(SInt(width = (width_w + width_a - 1)))
  })
  val mac = Module(new MAC (width_w, width_a, 1));
  mac.io.weight_i := io.weight_i;
  mac.io.act_i := io.act_i;
  mac.io.acc_i := SInt(0);
  mac.io.mul_en := io.mul_en;
  mac.io.add_en := false.B
  io.result_o := mac.io.result_o;
}

class ValExec_FMA(width_w: Int, width_a: Int, width_acc: Int) extends Module {
  val io = IO(new Bundle {
    val weight_i  = Input(SInt(width = width_w))
    val act_i     = Input(SInt(width = width_a))
    val acc_i     = Input(SInt(width = width_acc))
    val mul_en    = Input(Bool())
    val add_en    = Input(Bool())
    val result_o  = Output(SInt(width = max(width_acc, (width_w + width_a - 1))))
  })
  val mac = Module(new MAC (width_w, width_a, width_acc));
  mac.io.weight_i := io.weight_i;
  mac.io.act_i := io.act_i;
  mac.io.acc_i := io.acc_i;
  mac.io.mul_en := io.mul_en;
  mac.io.add_en := io.add_en;
  io.result_o := mac.io.result_o;
}

class ValExec_ADD_INT8 extends ValExec_ADD(1,8,8)
class ValExec_MUL_INT8 extends ValExec_MUL(8,8,1)
class ValExec_FMA_INT8 extends ValExec_FMA(8,8,8)
class ValExec_ADD_INT16 extends ValExec_ADD(1,16,16)
class ValExec_MUL_INT16 extends ValExec_MUL(16,16,1)
class ValExec_FMA_INT16 extends ValExec_FMA(16,16,16)
class ValExec_ADD_INT32 extends ValExec_ADD(1,32,32)
class ValExec_MUL_INT32 extends ValExec_MUL(32,32,1)
class ValExec_FMA_INT32 extends ValExec_FMA(32,32,32)
