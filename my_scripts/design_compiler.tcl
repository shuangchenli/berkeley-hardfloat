#set path {../../test_run_dir/pe.PEMain1497863354/}
#set top_module "SYSTOLIC2D"
set output_dir "../result"

## Design Setup
set search_path " . \
                  ../"
set search_path " . \
                  /ece/synopsys/synthesis/M-2016.12-SP5-4/libraries/syn \
                  /home/shuangchenli/FreePDK45/osu_soc/lib/files \
                  $search_path"
set target_library "gscl45nm.db"
set synthetic_library "dw_foundation.sldb"
set link_library   "* gscl45nm.db"
set symbol_library "/ece/synopsys/synthesis/J-2014.09-SP2/libraries/syn/ generic.sdb"

set cache_write "./cache_data/"
set cache_read "$cache_write"
define_design_lib DEFAULT -path ./analyzed
alias h history
history keep 100
set verilogout_no_tri true
set verilogout_show_unconnected_pins true
set hdlin_auto_save_templates true
define_name_rules BORG -allowed {A-Za-z0-9} \
                       -first_restricted "-" \
                       -last_restricted "-" \
                       -max_length 30

set timestamp [clock format [clock scan now] -format "%Y-%m-%d_%H-%M"]
set enable_page_mode false
set sh_new_variable_message false
set verilogout_no_tri true
set verilogout_show_unconnected_pins true
set hdlin_auto_save_templates true

############# Elaborate Design ################
read_file -format verilog ../${top_module}.v
current_design $top_module
link
uniquify -dont_skip_empty_designs
set set_ultra_optimization true
#set all_input_but_clock  [remove_from_collection [all_inputs] clock]

###################################################################
##...............define generated clock............................
create_clock -period  1.43 -name clk
set_clock_uncertainty -setup 0.1 clk
set_clock_uncertainty -hold  0.1 clk
#set_clock_transition 0.1 clk
##.............delay and drive strength on input ports.............
set_input_delay -max 0 -clock clk  [all_inputs]
set_input_delay -min 0 -clock clk  [all_inputs]
set_driving_cell -lib_cell DFFPOSX1 -pin Q [all_inputs]
##set_operating_conditions -min best -max worst
set_wire_load_mode segmented
set_load [load_of [get_pins gscl45nm/DFFPOSX1/D]] [all_outputs]
###################################################################
check_design
set_max_area 0
set_fix_hold [all_clocks]
set verilogout_show_unconnected_pins true
set verilogout_no_tri true
set_fix_multiple_port_nets -all -buffer_constants
set_svf $top_module.svf
compile_ultra -timing_high_effort_script -no_autoungroup
compile_ultra -incremental -timing_high_effort_script -no_autoungroup
#compile

## Report Design
define_name_rules verilog -case_insensitive
change_names -hierarchy -rules verilog
write  -h -format verilog -output "$output_dir/${top_module}_gate.v"
write -format ddc -hierarchy -output "$output_dir/${top_module}_gate.ddc"
write_sdc "$output_dir/${top_module}_gate.sdc"
write_sdf "$output_dir/${top_module}_gate.sdf"
redirect -append -tee "$output_dir/Report_area.txt" {report_area -nosplit -hierarchy}
redirect -append -tee "$output_dir/Report_power.txt" {report_power -hier -hier_level 100 -analysis_effort high}
redirect -append -tee "$output_dir/Report_power_netcell.txt" {report_power -net -cell -analysis_effort high -sort_mode dynamic_power}
redirect -append -tee "$output_dir/Report_clock.txt" {report_clock -nosplit}
redirect -append -tee "$output_dir/Report_timing_1.txt" {report_timing -path full -delay min -nworst 1 -max_paths 3 -significant_digits 2 -sort_by group}
redirect -append -tee "$output_dir/Report_timing_2.txt" {report_timing}
exit
