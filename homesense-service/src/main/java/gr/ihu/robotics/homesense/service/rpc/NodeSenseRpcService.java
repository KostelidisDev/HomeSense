package gr.ihu.robotics.homesense.service.rpc;

import gr.ihu.robotics.homesense.domain.model.NodeSenseData;
import gr.ihu.robotics.homesense.domain.model.NodeSenseStatus;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface NodeSenseRpcService {
    Try<List<NodeSenseStatus>> discover(String ipPrefix, Integer timeout);

    Try<NodeSenseStatus> getStatus(String ip, Integer timeout);

    Try<NodeSenseData> getData(String ip, Integer timeout);
}
