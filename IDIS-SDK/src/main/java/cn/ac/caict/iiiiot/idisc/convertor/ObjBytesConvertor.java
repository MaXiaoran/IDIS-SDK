package cn.ac.caict.iiiiot.idisc.convertor;
/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *© COPYRIGHT 2019 Corporation for Institute of Industrial Internet & Internet of Things (IIIIT);
 *                      All rights reserved. 
 * http://www.caict.ac.cn  
 * https://www.citln.cn/
 */
import cn.ac.caict.iiiiot.idisc.core.Attribute;
import cn.ac.caict.iiiiot.idisc.core.IdisCommunicationItems;
import cn.ac.caict.iiiiot.idisc.core.ServerInfo;
import cn.ac.caict.iiiiot.idisc.core.SiteInfo;
import cn.ac.caict.iiiiot.idisc.data.AdminInfo;
import cn.ac.caict.iiiiot.idisc.utils.Common;

public class ObjBytesConvertor extends BaseConvertor {
	public static final byte[] admInfoConvertToBytes(AdminInfo admInfo) {
		int infoLen = Common.TWO_SIZE + Common.FOUR_SIZE + Common.FOUR_SIZE + admInfo.admId.length;
		byte[] result_bytes = new byte[infoLen];
		int pos = 0;
		pos += write2Bytes(result_bytes, pos, admInfo.permissions);
		pos += writeByteArray(result_bytes, pos, admInfo.admId);
		pos += write4Bytes(result_bytes, pos, admInfo.admIdIndex);
		return null;
	}

	public static final byte[] siteInfoCovertToBytes(SiteInfo site) {
		int infoLen = 0;
		//data-Version+protocolVersion+serialNumber+primary+hashOption
		infoLen += 2 + 2 + 2 + 1 + 1; 
		// hashFilter
		infoLen += Common.FOUR_SIZE + (site.hashFilter == null ? 0 : site.hashFilter.length);
		infoLen += Common.FOUR_SIZE;
		if (site.attributes != null) {
			for (Attribute attribute : site.attributes) {
				infoLen += Common.FOUR_SIZE + attribute.name.length;
				infoLen += Common.FOUR_SIZE + attribute.value.length;
			}
		}
		infoLen += Common.FOUR_SIZE; // number of servers
		if (site.servers != null) {
			for (ServerInfo server : site.servers) {
				infoLen += Common.FOUR_SIZE; // serverId
				infoLen += Common.IP_ADDRESS_SIZE_SIXTEEN; 
				infoLen += Common.FOUR_SIZE + (server.publicKey == null ? 0 : server.publicKey.length); 

				infoLen += Common.FOUR_SIZE; // number of communicateItems
				if (server.communicationItems != null) {
					infoLen += (Common.FOUR_SIZE + 1 + 1) * server.communicationItems.length;
				}
			}
		}

		byte[] siteByteInfo = new byte[infoLen];
		int offset = 0;
		offset += write2Bytes(siteByteInfo, offset, site.dataFormatVersion);
		siteByteInfo[offset++] = site.majorProtocolVersion;
		siteByteInfo[offset++] = site.minorProtocolVersion;
		offset += write2Bytes(siteByteInfo, offset, site.serialNumber);
		siteByteInfo[offset++] = (byte) ((site.isPrimarySite ? SiteInfo.PRIMARY_SITE : 0)
				| (site.isMultiPrimarySite ? SiteInfo.MULTI_PRIMARY : 0));
		siteByteInfo[offset++] = site.hashOption;
		offset += writeByteArray(siteByteInfo, offset, site.hashFilter);

		if (site.attributes == null) {
			offset += write4Bytes(siteByteInfo, offset, 0);
		} else {
			offset += write4Bytes(siteByteInfo, offset, site.attributes.length);
			for (Attribute attribute : site.attributes) {
				offset += writeByteArray(siteByteInfo, offset, attribute.name);
				offset += writeByteArray(siteByteInfo, offset, attribute.value);
			}
		}

		if (site.servers == null) {
			offset += write4Bytes(siteByteInfo, offset, 0);
		} else {
			offset += write4Bytes(siteByteInfo, offset, site.servers.length);
			for (ServerInfo server : site.servers) {
				offset += write4Bytes(siteByteInfo, offset, server.serverId);

				System.arraycopy(server.ipBytes, 0, siteByteInfo,
						offset + Common.IP_ADDRESS_SIZE_SIXTEEN - server.ipBytes.length, server.ipBytes.length);
				offset += Common.IP_ADDRESS_SIZE_SIXTEEN;

				offset += writeByteArray(siteByteInfo, offset, server.publicKey);

				if (server.communicationItems == null) {
					offset += write4Bytes(siteByteInfo, offset, 0);
				} else {
					offset += write4Bytes(siteByteInfo, offset, server.communicationItems.length);
					for (IdisCommunicationItems item : server.communicationItems) {
						siteByteInfo[offset++] = item.type;
						siteByteInfo[offset++] = item.protocol;
						offset += write4Bytes(siteByteInfo, offset, item.port);
					}
				}
			}
		}
		return siteByteInfo;
	}
}
