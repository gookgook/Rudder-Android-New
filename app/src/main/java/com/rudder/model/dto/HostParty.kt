package com.rudder.model.dto

import java.io.Serializable

data class HostParty(
    var partyId: Int,
    var partyDetail: PartyDto.Companion.PartyDetail,
    var partyApplicants: List<PartyDto.Companion.PartyApplicant>,
    var partyGroupChatRoom: ChatDto.Companion.PartyGroupChatRoom,
    var partyOneToOneChatRooms: List<ChatDto.Companion.PartyOneToOneChatRoom>
): Serializable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
    companion object {
        fun from(partyId: Int): HostParty {
            return HostParty(
                partyId = partyId,
                partyDetail = PartyDto.Companion.PartyDetail.mock,
                partyApplicants = arrayListOf(),
                partyGroupChatRoom = ChatDto.Companion.PartyGroupChatRoom.mock,
                partyOneToOneChatRooms = arrayListOf()
            )
        }

        fun from(hostParty: HostParty): HostParty {
            return HostParty(
                partyId = hostParty.partyId,
                partyDetail = hostParty.partyDetail,
                partyApplicants = hostParty.partyApplicants,
                partyGroupChatRoom = hostParty.partyGroupChatRoom,
                partyOneToOneChatRooms = hostParty.partyOneToOneChatRooms
            )
        }
    }
}