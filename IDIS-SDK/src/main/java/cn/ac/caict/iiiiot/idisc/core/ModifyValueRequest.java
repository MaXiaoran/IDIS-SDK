package cn.ac.caict.iiiiot.idisc.core;
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
import cn.ac.caict.iiiiot.idisc.data.IdentifierValue;
import cn.ac.caict.iiiiot.idisc.security.AbstractAuthentication;
import cn.ac.caict.iiiiot.idisc.utils.MessageCommon;

public class ModifyValueRequest extends BaseRequest {

	public IdentifierValue[] values;

	public ModifyValueRequest(byte[] identifier, IdentifierValue value, AbstractAuthentication authInfo) {
		this(identifier, new IdentifierValue[] { value }, authInfo);
	}

	public ModifyValueRequest(byte[] identifier, IdentifierValue[] values, AbstractAuthentication authInfo) {
		super(identifier, MessageCommon.OC_MODIFY_VALUE, authInfo);
		this.values = values;
		this.isAdminRequest = false;
	}
}
