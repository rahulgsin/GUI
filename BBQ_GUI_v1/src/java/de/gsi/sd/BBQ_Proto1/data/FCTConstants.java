package de.gsi.sd.BBQ_Proto1.data;

public interface FCTConstants {

  static public final int ADC_COUNT = 4;

  static public final int ADC_VALUEMODE_RAW       = 0;
  static public final int ADC_VALUEMODE_VOLTAGE   = 1;
  
  static public final int TRIGGER_OUT_LEMO = 1;
  static public final int TRIGGER_OUT_LVDS = 2;
  
  static public final int VALUEMODE_RAW     = 0;
  static public final int VALUEMODE_VOLTAGE = 1;
  
  static public final int ACQ_MODE_CONTINUOUS = 0;
  static public final int ACQ_MODE_SINGLESHOT = 1;
  
  static public final int FEC_UNKNOWN      = 0x00000000; 
  static public final int FEC_CONNECTED    = 0x00000001; 
  static public final int FEC_DISCONNECTED = 0x00000002; 
  static public final int FEC_RECONNECTED  = 0x00000004; 
  
  static public final int TUNNEL_POWER     = 0x00000001;
  static public final int CONTROL_LOOP     = 0x00000002;
  static public final int LINK_TX          = 0x00000004;
  static public final int LINK_RX          = 0x00000008;
  static public final int STOPPED          = 0x00000100;
  static public final int WAIT             = 0x00000200;
  static public final int ACQUIRE          = 0x00000400;
  static public final int READOUT          = 0x00000800;
  static public final int NO_EVENTS        = 0x00001000;
  static public final int OUTPUT_INPROGRESS= 0x00002000;
  static public final int ADC_ARMED        = 0x00010000;
  static public final int ADC_BUSY         = 0x00020000;
  static public final int ADC_END_ADDRESS  = 0x00040000;
  
  static public final String[] ADCLABELS        = { "Label.FCT", "Label.HF", "Label.User1", "Label.User2" };
  static public final String[] FREQUENCY        = { "50 MHz", "100 MHz", "250MHz", "500MHz" };
  static public final double[] FREQUENCY_VALUES = { 50e6, 100e6, 250e6, 500e6 };
  static public final String[] ADCRANGE         = { "+-0.5V", "+-1.0V", "+-2.0V", "+-4.0V" };
  static public final String[] ADCVALUEMODE     = { "Raw", "Voltage" };
  static public final String[] FILEMODE         = { "Ascii", "Binary" };

}
