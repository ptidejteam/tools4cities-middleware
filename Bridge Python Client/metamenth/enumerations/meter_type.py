from enum import Enum


class MeterType(Enum):
    """
    Different types of meters used in a building.

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """
    ELECTRICITY = "Electricity"
    CHARGE_DISCHARGE = "ChargeDischarge"
    POWER = "Power"
    FLOW = "Flow"
    HEAT = "Heat"
    GAS = "Gas"

