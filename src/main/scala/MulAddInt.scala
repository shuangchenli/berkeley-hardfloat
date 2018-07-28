
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
import consts._
import scala.math.max

//----------------------------------------------------------------------------
//----------------------------------------------------------------------------
class MAC (val param_width_weight: Int, val param_width_act: Int, val param_width_acc: Int) extends Module {
  val io = IO(new Bundle {
    val weight_i  = Input(SInt(width = param_width_weight))
    val act_i     = Input(SInt(width = param_width_act))
    val acc_i     = Input(SInt(width = param_width_acc))
    val mul_en    = Input(Bool())
    val add_en    = Input(Bool())
    val result_o  = Output(SInt(width = max(param_width_acc, param_width_weight + param_width_act - 1) + 1))
  })
  when(io.mul_en && io.add_en) {
    val tmp = io.weight_i * io.act_i
    io.result_o := io.weight_i * io.act_i +& io.acc_i
  }
  .elsewhen(io.mul_en && !io.add_en) {
    io.result_o := io.weight_i * io.act_i
  }
  .elsewhen(!io.mul_en && io.add_en) {
    io.result_o := io.acc_i +& io.act_i
  }
  .otherwise {
    io.result_o := io.acc_i
  }
}

