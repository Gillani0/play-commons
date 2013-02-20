    ...........................................................................
    ........:$$$7..............................................................
    .....==7$$$I~~...........MMMMMMMMM....DMM..........MM,........MM7......MM..
    ...,?+Z$$$?=~,,:.........MMM,,,?MMM+..MMM.........,MMMM,......7MM,....MMM..
    ..:+?$ZZZ$+==:,:~........MMM.....MMM..MMM.........,MMDMMM:.....,MMI..MMM...
    ..++7ZZZZ?+++====,.......MMM....~MMM..MMM.........,MM??DMMM:....?MM,MMM....
    ..?+OZZZ7~~~~OOI=:.......MMMMMMMMMM...MMM.........,MM?II?MMM~....DMMMM.....
    ..+7OOOZ?+==+7Z$Z:.......MMM$$$I,.....MMM.........,MM??8MMM~......NMM......
    ..:OOOOO==~~~+OZ+........MMM..........MMM.........,MMDMMM~........NMM......
    ..,8OOOO+===+$$?,........MMM..........MMM.,,,,,...,MMMM:..........NMM......
    ,,+8OOOZIIIIII=,,,,,,,,,,MMM,,,,,,,,,,NMMMMMMMMM=,,MM:,,,,........8MM......
    ,,,:O8OO~+~:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
    ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
    ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
                                                      ASCII Art: GlassGiant.com

PLAY User Management and Access Control
=======================================
A library for PLAY to model users and permissions in RDF.

Data Model
----------
Permissions will be modelled in RDF using the Schemas [WebAccessControl (WAC)](http://www.w3.org/wiki/WebAccessControl), FOAF and SIOC. 

Work in progress:

![RDF model](http://yuml.me/diagram/plain;/class/[foaf:Person]-foaf:account>[sioc:UserAccount], [sioc:UserAccount]-owl:sameAs>[sioc:UserAccount], [sioc:Usergroup]-sioc:has_member>[sioc:UserAccount], [sioc:UserAccount]-sioc:function_of>[sioc:Role])

Considerations
--------------
Streams (accessed documents) are modelled as information resources (e.g., `http://streams.event-processing.org/ids/activityEvent`) because we grant access to the information in the stream. Elsewhere streams are modelled with their *non-information resource* (e.g., `http://streams.event-processing.org/ids/activityEvent#stream`) to attribute metadata to the real-world stream of events.
