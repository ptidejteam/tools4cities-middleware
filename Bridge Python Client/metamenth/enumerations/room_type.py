from enum import Enum


class RoomType(Enum):
    """
    Different types of rooms on a floor of a building.

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """
    OFFICE = "Office"
    KITCHEN = "Kitchen"
    BEDROOM = "Bedroom"
    LIVING = "Living"
    DINNING = "Dinning"
    PANTRY = "Pantry"
    STORAGE = "Storage"
    LIBRARY = "Library"
    WASHROOM = "Washroom"
    RESEARCH_LAB = "ResearchLab"
    COMPUTER_LAB = "ComputerLab"
    LAUNDRY = "Laundry"
    GARAGE = "Garage"
    DATA_CENTER = "DataCenter"
    MECHANICAL = "Mechanical"
    CLASSROOM = "Classroom"
    COMMON = "Common"
    LAVATORY = "Lavatory"
    HOUSE_KEEPING = "HouseKeeping"
    MAINTENANCE = "Maintenance"
    CAFETERIA = "Cafeteria"
    FILE = "File"
    LOUNGE = "Lounge"
    SUPPORT_SPACE = "SupportSpace"
    STAFF = "Staff"
    ENTERTAINMENT = "Entertainment"
    CONCIERGE = "Concierge"
    FIRE = "Fire"
    ELECTRIC = "Electric"
    MULTIFUNCTIONAL = "Multifunctional"
    TRIAGE = "Triage"
    LOCKER = "Locker"
    JANITOR_CLOSET = "JanitorCloset"
    PHOTO = "Photo"
    READING = "Reading"
    COMPUTER_SERVICE = "ComputerServices"
    COMMUNITY_SERVICE = "CommunityServices"
    CONSULTATION = "Consultation"
    SEMINAR = "Seminar"
    SPECIALISED_CLASSROOM = "SpecialisedClassroom"
    MAIL_ROOM = "MailRoom"
    BOOKSTORE = "BookStore"
    STUDY_ROOM = "StudyRoom"
    OTHER = "Other"

    def getValue(self):
        return self.value
    class Java:
        implements = ['com.middleware.enum.interfaces.IEnum']

